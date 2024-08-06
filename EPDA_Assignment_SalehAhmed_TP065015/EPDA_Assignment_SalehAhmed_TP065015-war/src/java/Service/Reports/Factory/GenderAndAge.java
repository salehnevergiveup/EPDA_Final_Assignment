/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Reports.Factory;

import Service.Reports.DTO.ReportsDTO;
import controllers.enums.ChartType;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import model.EJB.MyUserFacade;
import model.EJB.JobseekerInfoFacade;
import model.MyUser;
import model.JobseekerInfo;

public class GenderAndAge implements Reports {

    private String type;
    private Date start;
    private Date end;
    private MyUserFacade myUserFacade;
    private JobseekerInfoFacade jobseekerInfoFacade;

    public GenderAndAge(String type, Date start, Date end, MyUserFacade myUserFacade, JobseekerInfoFacade jobseekerInfoFacade) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.myUserFacade = myUserFacade;
        this.jobseekerInfoFacade = jobseekerInfoFacade;
    }

    @Override
    public ReportsDTO generateReport() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        List<MyUser> jobseekers = myUserFacade.findByRoleAndDateRange("Jobseeker", start, end);
        int totalJobseekers = jobseekers.size();
        Map<String, Integer> jobseekersByStatus = myUserFacade.countByStatusAndRoleAndDateRange("Jobseeker", start, end);
        List<Map<String, String>> jobseekersDetails = new ArrayList<>();
        Map<String, Integer> genderCount = new HashMap<>();
        Map<String, Integer> ageCount = new HashMap<>();

        for (MyUser jobseeker : jobseekers) {
            JobseekerInfo jobseekerInfo = jobseekerInfoFacade.findByUserId(jobseeker.getId());
            if (jobseekerInfo != null) {
                // Collect gender data
                String gender = jobseekerInfo.getGender();
                genderCount.put(gender, genderCount.getOrDefault(gender, 0) + 1);

                // Collect age data
                int age = jobseekerInfo.getAge();
                String ageRange = getAgeRange(age);
                ageCount.put(ageRange, ageCount.getOrDefault(ageRange, 0) + 1);
            }

            int totalComments = jobseeker.getJobseekersComments() == null ? 0 : jobseeker.getJobseekersComments().size();
            int totalApplications = jobseeker.getApplications() == null ? 0 : jobseeker.getApplications().size();
            int totalWarning = jobseeker.getJobSeekerWarnings() == null ? 0 : jobseeker.getJobSeekerWarnings().size();
            Map<String, String> jobseekerMap = new HashMap<>();
            jobseekerMap.put("Name", jobseeker.getName());
            jobseekerMap.put("Age",String.valueOf(jobseekerInfoFacade.findByUserId(jobseeker.getId()).getAge()));
            jobseekerMap.put("Created Date", dateFormatter.format(jobseeker.getCreatedAt()).toString());
            jobseekerMap.put("Status", jobseeker.getStatus());
            jobseekerMap.put("Updated Date", jobseeker.getUpdatedAt() == null ? "None" : dateFormatter.format(jobseeker.getUpdatedAt()).toString());
            jobseekerMap.put("Number of comments", String.valueOf(totalComments));
            jobseekerMap.put("Number of applications", String.valueOf(totalApplications));
            jobseekerMap.put("Number of Warnings", String.valueOf(totalWarning));
            jobseekersDetails.add(jobseekerMap);
        }

        List<String> chartLabels = new ArrayList<>(jobseekersByStatus.keySet());
        String chartLabelsString = chartLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(",", "[", "]"));
        List<Integer> chartData = new ArrayList<>(jobseekersByStatus.values());

        List<String> genderLabels = new ArrayList<>(genderCount.keySet());
        String genderLabelsString = genderLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(",", "[", "]"));
        List<Integer> genderData = new ArrayList<>(genderCount.values());

        List<String> ageLabels = new ArrayList<>(ageCount.keySet());
        String ageLabelsString = ageLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(",", "[", "]"));
        List<Integer> ageData = new ArrayList<>(ageCount.values());

        ReportsDTO reportDTO = new ReportsDTO();
        reportDTO.setTypeOfChart1(ChartType.BAR.getType());
        reportDTO.setTotalCount(totalJobseekers);
        reportDTO.setCountByCategory(jobseekersByStatus);
        reportDTO.setDetails(jobseekersDetails);
        reportDTO.setChartLabels(chartLabelsString);
        reportDTO.setChartData(chartData);
        reportDTO.setTypeOfChart2(ChartType.DOUGHNUT.getType());
        reportDTO.setSecondaryChartLabels(genderLabelsString);
        reportDTO.setSecondaryChartData(genderData);
        reportDTO.setChartLabels(ageLabelsString);
        reportDTO.setChartData(ageData);

        return reportDTO;
    }

    private String getAgeRange(int age) {
        if (age < 20) {
            return "Below 20";
        } else if (age < 30) {
            return "20-29";
        } else if (age < 40) {
            return "30-39";
        } else if (age < 50) {
            return "40-49";
        } else {
            return "50 and above";
        }
    }
}