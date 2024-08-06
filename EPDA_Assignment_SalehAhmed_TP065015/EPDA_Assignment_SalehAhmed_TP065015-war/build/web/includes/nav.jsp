<%-- 
    Document   : nav
    Created on : Jul 3, 2024, 11:39:33 AM
    Author     : saleh
--%>

<%@page import="model.MyUser"%>
<%@page import="model.MyRole"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Auth"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>

<div class="navbar bg-base-100 mb-4 shadow-xl rounded-box print:hidden" x-data="{ open: false }">
    <div class="navbar-start">
        <a class="btn btn-ghost normal-case text-xl" href='/EPDA_Assignment_SalehAhmed_TP065015-war/login.jsp'>Jobs App</a>       
    </div>
    <div class="navbar-center hidden lg:flex space-x-4">
        <!--Rating,Feedback-->
        <%
            MyUser user = Auth.user(request);
            if (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) {
                out.println("<a class='btn btn-ghost uppercase' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.DASHBOARD.getPath()).get() + "'>Home</a>");
            }
            if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Job", "Read Job", "Update Job", "Delete Job")))) {
                out.println("<a class='btn btn-ghost uppercase' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Job</a>");
            }

            if (Auth.canAny(request, new ArrayList(Arrays.asList("Create User", "Read User", "Update User", "Delete User")))) {
                out.println("<a class='btn btn-ghost uppercase' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>User</a>");
            }

            if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Application", "Read Application", "Update Application", "Delete Application")))) {
                out.println("<a class='btn btn-ghost uppercase' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Application</a>");
            }

            if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Feedback", "Read Feedback", "Update Feedback", "Delete Feedback")))) {
                out.println("<a class='btn btn-ghost uppercase' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Feedback</a>");
            }

            if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Comment", "Read Comment", "Update Comment", "Delete Comment")))) {
                out.println("<a class='btn btn-ghost uppercase' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.COMMENTS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Comment</a>");
            }

            if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Warning", "Read Warning", "Update Warning", "Delete Warning")))) {
                out.println("<a class='btn btn-ghost uppercase' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.WARNINGS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Warning</a>");
            }

            if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Report", "Read Report", "Update Report", "Delete Report")))) {
                out.println("<a class='btn btn-ghost uppercase' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.REPORTS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Report</a>");
            }
        %>
    </div>
    <div class="navbar-end">
        <div class="dropdown dropdown-end">
            <label tabindex="0" class="btn btn-ghost btn-circle">
                <div class="w-6 rounded-full">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                </div>
            </label>
            <ul tabindex="0" class="mt-3 p-2 shadow menu menu-compact dropdown-content bg-base-100 rounded-box w-52">
                <li class='cursor-default no-animation'>
                    <a href=<%=new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.PROFILES.getPath()).add(ServletFile.VIEW.getPath()).get()%>> <i class='fas fa-user mr-2'></i>Profile</a>
                </li>
                <div class='divider h-1 my-2'></div>
                <%
                   if(user.can("Start App")) {
                    out.println("<li><a href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(JspFile.BOOTSTRAP_APP.getPath()).get() + "'><i class='fas fa-paper-plane mr-2'></i> Bootstrap App</a></li>"); 
                    }
                %>
                <li><a href=<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.LOGOUT.getPath()).get()%>><i class='fas fa-sign-out-alt mr-2'></i>Logout</a></li>
            </ul>
        </div>
        <button @click="open = !open" class="lg:hidden text-gray-700 hover:text-gray-900 focus:outline-none focus:text-gray-900 ml-4">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
            </svg>
        </button>
    </div>
    <div class="lg:hidden" x-show="open" @click.away="open = false" x-cloak>
        <div class="pt-2 pb-3 space-y-1">
            <%
                if (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) {
                    out.println("<a class='btn btn-ghost uppercase' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.DASHBOARD.getPath()).get() + "'>Home</a>");
                }
                if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Job", "Read Job", "Update Job", "Delete Job")))) {
                    out.println("<a class='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-200' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Job</a>");
                }
                if (Auth.canAny(request, new ArrayList(Arrays.asList("Create User", "Read User", "Update User", "Delete User")))) {
                    out.println("<a class='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-200' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>User</a>");
                }

                if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Application", "Read Application", "Update Application", "Delete Application")))) {
                    out.println("<a class='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-200' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Application</a>");
                }

                if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Feedback", "Read Feedback", "Update Feedback", "Delete Feedback")))) {
                    out.println("<a class='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-200' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Feedback</a>");
                }

                if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Comment", "Read Comment", "Update Comment", "Delete Comment")))) {
                    out.println("<a class='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-200' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.COMMENTS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Comment</a>");
                }

                if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Warning", "Read Warning", "Update Warning", "Delete Warning")))) {
                    out.println("<a class='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-200' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.WARNINGS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Warning</a>");
                }

                if (Auth.canAny(request, new ArrayList(Arrays.asList("Create Report", "Read Report", "Update Report", "Delete Report")))) {
                    out.println("<a class='block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-200' href='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.REPORTS.getPath()).add(ServletFile.INDEX.getPath()).get() + "'>Report</a>");
                }
            %>
        </div>
    </div>
</div>
