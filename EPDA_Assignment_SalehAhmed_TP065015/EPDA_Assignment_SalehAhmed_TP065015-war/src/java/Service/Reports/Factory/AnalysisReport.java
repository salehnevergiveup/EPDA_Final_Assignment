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
import model.Comment;
import model.EJB.CommentFacade;
import model.EJB.FeedbackFacade;
import model.Feedback;

/**
 *
 * @author saleh
 */
public class AnalysisReport  implements Reports {

    private String type;
    private Date start;
    private Date end;
    private FeedbackFacade feedbackFacade;
    private CommentFacade commentFacade;

    public AnalysisReport(String type, Date start, Date end, FeedbackFacade feedbackFacade, CommentFacade commentFacade) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.feedbackFacade = feedbackFacade;
        this.commentFacade = commentFacade;
    }

 @Override
    public ReportsDTO generateReport() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        // Get feedback data
        int totalFeedbacks = feedbackFacade.getTotalFeedbacks(start, end);
        int totalPositiveFeedbacks = feedbackFacade.getTotalPositiveFeedbacks(start, end);
        int totalNegativeFeedbacks = feedbackFacade.getTotalNegativeFeedbacks(start, end);
        List<Feedback> feedbacks = feedbackFacade.findByDateRange(start, end);

        // Get comment data
        int totalComments = commentFacade.getTotalComments(start, end);
        List<Comment> comments = commentFacade.findByDateRange(start, end);

        // Prepare feedback details table
        List<Map<String, String>> feedbackDetails = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            Map<String, String> feedbackMap = new HashMap<>();
            feedbackMap.put("Customer Name", feedback.getCustomer().getName());
            feedbackMap.put("Feedback Date", dateFormatter.format(feedback.getCreatedAt()));
            feedbackDetails.add(feedbackMap);
        }

        // Prepare comment details table
        List<Map<String, String>> commentDetails = new ArrayList<>();
        for (Comment comment : comments) {
            Map<String, String> commentMap = new HashMap<>();
            commentMap.put("Jobseeker Name", comment.getJobSeeker().getName());
            commentMap.put("Comment Date", dateFormatter.format(comment.getCreatedAt()));
            commentMap.put("Updated Date", comment.getUpdatedAt() == null ? "None" : dateFormatter.format(comment.getUpdatedAt()));
            commentDetails.add(commentMap);
        }

        // Prepare chart data for feedback
        Map<String, Integer> feedbackSummary = new HashMap<>();
        feedbackSummary.put("Total Feedbacks", totalFeedbacks);
        feedbackSummary.put("Positive Feedbacks", totalPositiveFeedbacks);
        feedbackSummary.put("Negative Feedbacks", totalNegativeFeedbacks);

        List<String> feedbackChartLabels = new ArrayList<>(feedbackSummary.keySet());
        List<Integer> feedbackChartData = new ArrayList<>(feedbackSummary.values());

        String feedbackChartLabelsString = feedbackChartLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(",", "[", "]"));

        // Prepare combined chart data for comments and feedbacks
        Map<String, Integer> commentFeedbackSummary = new HashMap<>();
        commentFeedbackSummary.put("Total Comments", totalComments);
        commentFeedbackSummary.put("Total Feedbacks", totalFeedbacks);

        List<String> commentFeedbackChartLabels = new ArrayList<>(commentFeedbackSummary.keySet());
        List<Integer> commentFeedbackChartData = new ArrayList<>(commentFeedbackSummary.values());

        String commentFeedbackChartLabelsString = commentFeedbackChartLabels.stream()
                .map(label -> "\"" + label + "\"")
                .collect(Collectors.joining(",", "[", "]"));

        ReportsDTO reportDTO = new ReportsDTO();
        reportDTO.setTotalCount(totalFeedbacks + totalComments);
        reportDTO.setCountByCategory(feedbackSummary);
        reportDTO.setDetails(feedbackDetails);
        reportDTO.setChartLabels(feedbackChartLabelsString);
        reportDTO.setChartData(feedbackChartData);

        reportDTO.setSecondaryChartLabels(commentFeedbackChartLabelsString);
        reportDTO.setSecondaryChartData(commentFeedbackChartData);
        reportDTO.setSecondaryDetails(commentDetails);
        reportDTO.setTypeOfChart1(ChartType.BAR.getType());  
        reportDTO.setTypeOfChart2(ChartType.BAR.getType());

        return reportDTO;
    }
}