/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Reports.DTO;

import java.util.List;
import java.util.Map;

/**
 *
 * @author saleh
 */
public class ReportsDTO {
    private int totalCount;  

    public String getTypeOfChart1() {
        return typeOfChart1;
    }

    public void setTypeOfChart1(String typeOfChart1) {
        this.typeOfChart1 = typeOfChart1;
    }

    public String getTypeOfChart2() {
        return typeOfChart2;
    }

    public void setTypeOfChart2(String typeOfChart2) {
        this.typeOfChart2 = typeOfChart2;
    }
    private Map<String, Integer> countByCategory;  
    private List<Map<String, String>> details;
    private List<Map<String, String>> secondaryDetails;
    private String chartLabels;  
    private List<Integer> chartData; 
    private String typeOfChart1;  
    private String typeOfChart2;  
    private String secondaryChartLabels;
    private List<Integer> secondaryChartData;
    
    public List<Map<String, String>> getDetails() {
        return details;
    }

    public void setDetails(List<Map<String, String>> details) {
        this.details = details;
    }

    public List<Map<String, String>> getSecondaryDetails() {
        return secondaryDetails;
    }

    public void setSecondaryDetails(List<Map<String, String>> secondaryDetails) {
        this.secondaryDetails = secondaryDetails;
    }


    public int getTotalCount() {
        return totalCount;
    }

    public String getSecondaryChartLabels() {
        return secondaryChartLabels;
    }

    public void setSecondaryChartLabels(String secondaryChartLabels) {
        this.secondaryChartLabels = secondaryChartLabels;
    }

    public List<Integer> getSecondaryChartData() {
        return secondaryChartData;
    }

    public void setSecondaryChartData(List<Integer> secondaryChartData) {
        this.secondaryChartData = secondaryChartData;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Map<String, Integer> getCountByCategory() {
        return countByCategory;
    }

    public void setCountByCategory(Map<String, Integer> countByCategory) {
        this.countByCategory = countByCategory;
    }


    public String getChartLabels() {
        return chartLabels;
    }

    public void setChartLabels(String chartLabels) {
        this.chartLabels = chartLabels;
    }

    public List<Integer> getChartData() {
        return chartData;
    }

    public void setChartData(List<Integer> chartData) {
        this.chartData = chartData;
    }
    
}
