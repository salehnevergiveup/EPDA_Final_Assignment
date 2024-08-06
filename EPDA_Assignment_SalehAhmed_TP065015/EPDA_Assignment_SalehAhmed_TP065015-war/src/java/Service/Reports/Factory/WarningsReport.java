/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Reports.Factory;

import Service.Reports.DTO.ReportsDTO;
import controllers.enums.ChartType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.EJB.WarningFacade;
import model.Warning;

/**
 *
 * @author saleh
 */
public class WarningsReport implements Reports {

    private String type;
    private Date start;
    private Date end;
    private WarningFacade warningFacade;

    public WarningsReport(String type, Date start, Date end, WarningFacade warningFacade) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.warningFacade = warningFacade;
    }

    @Override
    public ReportsDTO generateReport() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        List<Warning> warnings = warningFacade.findByDateRange(start, end);
        int totalWarnings = warningFacade.getTotalWarnings(start, end);
        int totalJobseekersWithWarnings = warningFacade.getTotalJobseekersWithWarnings(start, end);
        int totalJobseekersWithoutWarnings = warningFacade.countDistinctJobseekers() - totalJobseekersWithWarnings;

        Map<String, Integer> warningsByManager = warningFacade.countByManager();
        Map<String, Integer> jobseekersByManager = warningFacade.countJobseekersByManager();

        List<Map<String, String>> warningDetails = new ArrayList<>();
        for (Warning warning : warnings) {
            Map<String, String> warningMap = new HashMap<>();
            warningMap.put("Manager Name", warning.getManagement().getName());
            warningMap.put("Jobseeker Name", warning.getJobseeker().getName());
            warningMap.put("Warning Date", dateFormatter.format(warning.getCreatedAt()));
            warningDetails.add(warningMap);
        }

        // Prepare chart data
        Map<String, Integer> jobseekersWithAndWithoutWarnings = new HashMap<>();
        jobseekersWithAndWithoutWarnings.put("With Warnings", totalJobseekersWithWarnings);
        jobseekersWithAndWithoutWarnings.put("Without Warnings", totalJobseekersWithoutWarnings);

        List<String> chartLabels = new ArrayList<>(jobseekersWithAndWithoutWarnings.keySet());
        List<Integer> chartData = new ArrayList<>(jobseekersWithAndWithoutWarnings.values());

        String chartLabelsString = chartLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(",", "[", "]"));

        ReportsDTO reportDTO = new ReportsDTO();
        reportDTO.setTypeOfChart1(ChartType.BAR.getType());
        reportDTO.setTypeOfChart2(ChartType.BAR.getType());
        reportDTO.setTotalCount(totalWarnings);
        reportDTO.setCountByCategory(warningsByManager);
        reportDTO.setDetails(warningDetails);
        reportDTO.setChartLabels(chartLabelsString);
        reportDTO.setChartData(chartData);

        List<String> secondaryChartLabels = new ArrayList<>(jobseekersByManager.keySet());
        List<Integer> secondaryChartData = new ArrayList<>(jobseekersByManager.values());

        reportDTO.setSecondaryChartLabels(secondaryChartLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(",", "[", "]")));
        reportDTO.setSecondaryChartData(secondaryChartData);
        return reportDTO;
    }
}