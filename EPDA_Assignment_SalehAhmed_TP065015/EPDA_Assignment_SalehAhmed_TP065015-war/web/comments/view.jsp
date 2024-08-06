<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="middlewares.Gate"%>
<%@page import="helpers.Auth"%>
<%@page import="helpers.ContentCreator"%>
<%@page import="model.MyUser"%>
<%@page import="model.MyUser"%>
<%@page import="model.Comment"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Comment</title>
        <jsp:include page="../includes/style.jsp"/> 
    </head>
    <body>

        <%Gate.authorise(request, response, "Read Comment");%>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        
        <%
            Comment comment = (Comment) request.getAttribute("comment");
            MyUser user = Auth.user(request);
            out.println(
                new ContentCreator()
                .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
                .start(ContentCreator.Wrapper.DIV, "", "")
                .action("<i class='fas fa-home mr-2'></i>Main", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.COMMENTS.getPath()).add(ServletFile.INDEX.getPath()).get(), "", "", "mr-2 bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded", "")
                .end(ContentCreator.Wrapper.DIV)
                .space(20)
                .start(ContentCreator.Wrapper.CONTAINER, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg", "")
                .header("Comment Details", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
                .start(ContentCreator.Wrapper.DIV, "text-center", "")
                .paragraph("User Name: " + comment.getJobSeeker().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                .paragraph("Comment Text: " + comment.getContent(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                .paragraph("Created At: " + comment.getCreatedAt(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                .paragraph("For User: " + comment.getCustomer().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "") // Assuming there is a method getForUser()
                .start(ContentCreator.Wrapper.DIV, "flex items-center mt-4", "")
                .paragraph("Rating: ", "text-left text-lg text-gray-700", "")
                .getContent()
            );

//             Adding star rating
            int rating = comment.getRating();
            for (int i = 1; i <= 5; i++) {
                if (i <= rating) {
                    out.println("<i class='fas fa-star text-yellow-500'></i>"); // Filled star
                } else {
                    out.println("<i class='far fa-star text-gray-400'></i>"); // Empty star
                }
            }

            out.println(
                    new ContentCreator()
                            .end(ContentCreator.Wrapper.DIV)
                            .end(ContentCreator.Wrapper.CONTAINER)
                            .space(60)
                            .getContent()
            );
        %>
    </body>
</html>
