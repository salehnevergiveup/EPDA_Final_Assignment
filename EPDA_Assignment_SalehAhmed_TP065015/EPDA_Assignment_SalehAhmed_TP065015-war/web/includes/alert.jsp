<%@page import="helpers.HttpHelper"%>
<% if (HttpHelper.getOnce(request, "validation_error") != null) {
        out.println("<div class=\"alert w-2/3 mx-auto shadow-lg alert-error my-2 \"><div><span>Error! Your form did not pass the validation.</span></div></div>");
    } else if (HttpHelper.getSession(request, "error") != null) {
        out.println("<div class=\"alert w-2/3 mx-auto shadow-lg alert-error my-2 \"><div><span>Error! " + HttpHelper.getOnce(request, "error") + "</span></div></div>");
    } else if (HttpHelper.getSession(request, "success")!=  null){
        out.println("<div class=\"alert w-2/3 mx-auto shadow-lg alert-success my-2 \"><div><span>" + HttpHelper.getOnce(request, "success") + "</span></div></div>");
    }
%>