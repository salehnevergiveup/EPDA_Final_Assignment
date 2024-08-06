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
import model.EJB.MyJobFacade;
import model.MyJob;
import model.Report;

/**
 *
 * @author saleh
 */
public class JobsReport implements Reports {
    private String type;
    private Date start;
    private Date end;
    private MyJobFacade myJobFacade;

    public JobsReport(String type, Date start, Date end, MyJobFacade myJobFacade) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.myJobFacade = myJobFacade;
    }

    @Override
    public ReportsDTO generateReport() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        List<MyJob> jobs = myJobFacade.findByDateRange(start, end);
        int totalJobs = jobs.size();
        Map<Boolean, Integer> jobsByStatus = myJobFacade.countByStatusAndDateRange(start, end);

        List<Map<String, String>> jobDetails = new ArrayList<>();
        for (MyJob job : jobs) {
            Map<String, String> jobMap = new HashMap<>();
            jobMap.put("Job Name", job.getTitle());
            jobMap.put("Company Name: ", job.getCustomer().getName());
            jobMap.put("Created Date", dateFormatter.format(job.getCreatedAt()));
            jobMap.put("Updated Date", job.getUpdatedAt() == null ? "None" : dateFormatter.format(job.getUpdatedAt()));
            jobMap.put("Status", job.getStatus() ? "Active":  "Inactive  ");
            jobMap.put("Number of Applications", String.valueOf(job.getApplications().size()));
            jobDetails.add(jobMap);
        }

         Map<String, Integer> jobsByStatusString = new HashMap<>();
        for (Map.Entry<Boolean, Integer> entry : jobsByStatus.entrySet()) {
            String statusString = entry.getKey() ? "Active" : "Inactive";
            jobsByStatusString.put(statusString, entry.getValue());
        }

        List<String> chartLabels = new ArrayList<>(jobsByStatusString.keySet());
        List<Integer> chartData = new ArrayList<>(jobsByStatusString.values());

        String chartLabelsString = chartLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(",", "[", "]"));

        ReportsDTO reportDTO = new ReportsDTO();
        reportDTO.setTotalCount(totalJobs);
        reportDTO.setTypeOfChart1(ChartType.BAR.getType());
        reportDTO.setCountByCategory(jobsByStatusString);
        reportDTO.setDetails(jobDetails);
        reportDTO.setChartLabels(chartLabelsString);
        reportDTO.setChartData(chartData);

        return reportDTO;
    }
}
