package Service.Reports.Factory;

import Service.Reports.DTO.ReportsDTO;
import Service.Reports.Factory.Reports;
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

public class CustomerReport implements Reports {
    private String type;
    private Date start;
    private Date end;
    private MyUserFacade myUserFacade;

    public CustomerReport(String type, Date start, Date end, MyUserFacade myUserFacade) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.myUserFacade = myUserFacade;
    }

    @Override
    public ReportsDTO generateReport() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        List<MyUser> customers = myUserFacade.findByRoleAndDateRange("Customer", start, end);
        int totalCustomers = customers.size();
        Map<String, Integer> customersByStatus = myUserFacade.countByStatusAndRoleAndDateRange("Customer", start, end);
        List<Map<String, String>> customerDetails = new ArrayList<>();
        for (MyUser customer : customers) {
            int totalJobs =  customer.getJobs()== null?  0 : customer.getJobs().size();
            int totalFeedbacks = customer.getCustomerFeedbacks() == null? 0 : customer.getCustomerFeedbacks().size();
            Map<String, String> customerMap = new HashMap<>();
            customerMap.put("Name", customer.getName());
            customerMap.put("Created Date",  dateFormatter.format(customer.getCreatedAt()).toString());
            customerMap.put("Status", customer.getStatus());
            customerMap.put("Updated Date", customer.getUpdatedAt() == null? "None":  dateFormatter.format(customer.getUpdatedAt()).toString());
            customerMap.put("Number of Jobs", String.valueOf(totalJobs));
            customerMap.put("Number of Feedbacks", String.valueOf(totalFeedbacks));
            customerDetails.add(customerMap);
        }

        List<String> chartLabels = new ArrayList<>(customersByStatus.keySet());
        String chartLabelsString = chartLabels.stream()
        .map(label -> "\"" + label + "\"")
        .collect(Collectors.joining(",", "[", "]"));
        List<Integer> chartData = new ArrayList<>(customersByStatus.values());
        ReportsDTO reportDTO = new ReportsDTO();
        reportDTO.setTypeOfChart1(ChartType.BAR.getType());
        reportDTO.setTotalCount(totalCustomers);
        reportDTO.setCountByCategory(customersByStatus);
        reportDTO.setDetails(customerDetails);
        reportDTO.setChartLabels(chartLabelsString);
        reportDTO.setChartData(chartData);

        return reportDTO;
    }
}
