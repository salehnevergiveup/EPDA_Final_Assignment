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
import model.EJB.MyUserFacade;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class JobseekersReport implements Reports {

    private String type;
    private Date start;
    private Date end;
    private MyUserFacade myUserFacade;

    public JobseekersReport(String type, Date start, Date end, MyUserFacade myUserFacade) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.myUserFacade = myUserFacade;
    }

    @Override
    public ReportsDTO generateReport() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        List<MyUser> jobseekers = myUserFacade.findByRoleAndDateRange("Jobseeker", start, end);
        int totalJobseekers = jobseekers.size();
        Map<String, Integer> jobseekersByStatus = myUserFacade.countByStatusAndRoleAndDateRange("Jobseeker", start, end);
        List<Map<String, String>> jobseekersDetails = new ArrayList<>();
        for (MyUser jobseeker : jobseekers) {
            int totalComments = jobseeker.getJobseekersComments() == null ? 0 : jobseeker.getJobseekersComments().size();
            int totalApplications = jobseeker.getApplications() == null ? 0 : jobseeker.getApplications().size();
            int totalWarning = jobseeker.getJobSeekerWarnings()== null ? 0 : jobseeker.getJobSeekerWarnings().size();
            Map<String, String> jobseekerMap = new HashMap<>();
            jobseekerMap.put("Name", jobseeker.getName());
            jobseekerMap.put("Created Date", dateFormatter.format(jobseeker.getCreatedAt()).toString());
            jobseekerMap.put("Status", jobseeker.getStatus());
            jobseekerMap.put("Updated Date", jobseeker.getUpdatedAt() == null ? "None" : dateFormatter.format(jobseeker.getUpdatedAt()).toString());
            jobseekerMap.put("Number of comments", String.valueOf(totalComments));
            jobseekerMap.put("Number of applicaitons", String.valueOf(totalApplications));
            jobseekerMap.put("Number of Warning", String.valueOf(totalWarning));
            jobseekersDetails.add(jobseekerMap);
        }

        List<String> chartLabels = new ArrayList<>(jobseekersByStatus.keySet());
        String chartLabelsString = chartLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(",", "[", "]"));
        List<Integer> chartData = new ArrayList<>(jobseekersByStatus.values());
        ReportsDTO reportDTO = new ReportsDTO();
        reportDTO.setTypeOfChart1(ChartType.BAR.getType());
        reportDTO.setTotalCount(totalJobseekers);
        reportDTO.setCountByCategory(jobseekersByStatus);
        reportDTO.setDetails(jobseekersDetails);
        reportDTO.setChartLabels(chartLabelsString);
        reportDTO.setChartData(chartData);

        return reportDTO;
    }
}
