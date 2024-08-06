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
import model.Application;
import model.EJB.ApplicationFacade;


/**
 *
 * @author saleh
 */
public class ApplicationReport implements Reports{
    public final ApplicationFacade applcationFacade;
    private Date start;  
    private Date end;  
    private String type; 
    
    public ApplicationReport(String type, Date start, Date end,ApplicationFacade applcationFacade) {
        this.type = type;
        this.start = start;  
        this.end = end; 
        this.applcationFacade = applcationFacade;
    }

    @Override
    public ReportsDTO generateReport() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        
        List<Application> applications = applcationFacade.getApplicationsByDateRange(start, end);
        int totalApplications = applcationFacade.getTotalApplications(start, end);
        Map<String, Integer> applicationsByStatus = applcationFacade.getApplicationsByStatus(start, end);
        List<Map<String, String>> applicationDetails = new ArrayList<>();
        for (Application application : applications) {
            Map<String, String> applicationMap = new HashMap<>();
            applicationMap.put("Application Date", dateFormatter.format(application.getCreatedAt()).toString());
            applicationMap.put("Status", application.getStatus());
            applicationMap.put("Jobseeker", application.getJobSeeker().getName());
            applicationMap.put("Job", application.getJob().getTitle());
            applicationMap.put("Company", application.getJob().getCustomer().getName());
            applicationDetails.add(applicationMap);
        }        List<String> chartLabels = new ArrayList<>(applicationsByStatus.keySet());
       
        String chartLabelsString = chartLabels.stream()
                .map(label -> "\"" + label + "\"") 
                .collect(Collectors.joining(",", "[", "]"));
        
        List<Integer> chartData = new ArrayList<>(applicationsByStatus.values());

        ReportsDTO reportDTO = new ReportsDTO();
        reportDTO.setTypeOfChart1(ChartType.BAR.getType());  
        reportDTO.setTotalCount(totalApplications);
        reportDTO.setCountByCategory(applicationsByStatus);
        reportDTO.setDetails(applicationDetails);
        reportDTO.setChartLabels(chartLabelsString);
        reportDTO.setChartData(chartData);

        return reportDTO;
    }
    
}
