<%-- 
    Document   : create
    Created on : Jul 3, 2024, 9:11:47 PM
    Author     : saleh
--%>
<%@page import="middlewares.Gate"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Auth"%>
<%@page import="controllers.enums.AccountStatus"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.EJB.MyUserFacade"%>
<%@page import="model.Model"%>
<%@page import="model.MyUser"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="helpers.HttpHelper"%>  
<%@page import="helpers.ContentCreator" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create User</title>
        <jsp:include page="../includes/style.jsp"/> 
        <script>
            function toggleRoleFields() {
                var selectedRole = document.querySelector('input[name="role"]:checked').value;
                var jobseekerFields = document.getElementById('jobseekerFields');
                var customerFields = document.getElementById('customerFields');

                jobseekerFields.style.display = 'none';
                customerFields.style.display = 'none';

                if (selectedRole === 'Jobseeker') {
                    jobseekerFields.style.display = 'block';
                } else if (selectedRole === 'Customer') {
                    customerFields.style.display = 'block';
                }
            }

            document.addEventListener('DOMContentLoaded', function () {
                var selectedRoleRadio = document.querySelector('input[name="role"]:checked');
                if (selectedRoleRadio) {
                    toggleRoleFields();
                }
            });
        </script>
    </head>
    <body>
        <%Gate.authorise(request, response, "Create User");%>
        <jsp:include page="../includes/nav.jsp"/>
        <% MyUser user = Auth.user(request);%> 
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <div class="flex justify-end p-4">
            <a href="#" class="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-2 rounded inline-flex items-center">
                <i class='fas fa-home mr-2'></i>main
            </a>
        </div>
        <div class="max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg mt-5">
            <h2 class="text-2xl font-bold mb-6 text-center">Create User</h2>
            <form method="POST" action="<%=new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.CREATE.getPath()).get()%>">
                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="name">Name:</label>
                    <input type="text" id="name" name="name" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="userName">Username:</label>
                    <input type="text" id="userName" name="userName" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="email">Email:</label>
                    <input type="email" id="email" name="email" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="phoneNumber">Phone Number:</label>
                    <input type="text" id="phoneNumber" name="phoneNumber" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="password">Password:</label>
                    <input type="password" id="password" name="password" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="confPassword">Confirm Password:</label>
                    <input type="password" id="confPassword" name="confPassword" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                </div>

                <!-- Radio buttons for role selection -->
                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2">Role:</label>
                    <%if (user.getRole().getName().equalsIgnoreCase("Admin")) {%> 
                    <label class="inline-flex items-center mt-3">
                        <input type="radio" class="form-radio h-5 w-5 text-gray-600" name="role" value="Management" onchange="toggleRoleFields();">
                        <span class="ml-2 text-gray-700">Management</span>
                    </label>

                    <%}%>
                    <%if (user.getRole().getName().equalsIgnoreCase("Management")) {%> 
                    <label class="inline-flex items-center mt-3">
                        <input type="radio" class="form-radio h-5 w-5 text-gray-600" name="role" value="Jobseeker" onchange="toggleRoleFields();">
                        <span class="ml-2 text-gray-700">Jobseeker</span>
                    </label>
                    <label class="inline-flex items-center mt-3">
                        <input type="radio" class="form-radio h-5 w-5 text-gray-600" name="role" value="Customer" onchange="toggleRoleFields();">
                        <span class="ml-2 text-gray-700">Customer</span>
                    </label>
                    <%}%>
                </div>
                <!-- Conditional fields for Customer -->
                <div id="customerFields" style="display:none;" class="mb-4">
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="companyName">Company Name:</label>
                        <input type="text" id="companyName"  name="companyName" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="website">Website:</label>
                        <input type="text" id="website"  name="website" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="address">Address:</label>
                        <input type="text" id="address"  name="companyaddress" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                </div>
                <!-- Conditional fields for Jobseeker -->

                <div id="jobseekerFields" style="display:none;" class="mb-4">
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="age">Age</label>
                        <input type="number" id="age" name="age" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="address">Address:</label>
                        <input type="text" id="addressJS" name="jobseekeraddress"  class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="hobbies">Hobbies:</label>
                        <input type="text" id="hobbies" name="hobbies"  class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="skills">Skills:</label>
                        <input type="text" id="skills" name="skills"  class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div class="flex items-center">
                        <label class="flex items-center mr-4">
                            <input type="radio" name="gender" value="Male"  class="form-radio h-5 w-5 text-blue-600">
                            <span class="ml-2 text-gray-700">Male</span>
                        </label>
                        <label class="flex items-center">
                            <input type="radio" name="gender" value="Female"  class="form-radio h-5 w-5 text-pink-600">
                            <span class="ml-2 text-gray-700">Female</span>
                        </label>
                    </div>
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 text-sm font-bold mb-2" for="status">Status:</label>
                    <select id="status" name="status" class="block appearance-none w-full bg-white border border-gray-200 text-gray-700 py-3 px-4 pr-8 rounded leading-tight focus:outline-none focus:bg-white focus:border-gray-500">
                        <option value="Active">Active</option>
                        <option value="Pending">Pending</option>
                        <option value="Rejected">Rejected</option>
                        <option value="Suspended">Suspended</option>
                    </select>
                </div>
                <button type="submit" class="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Create</button>
            </form>
        </div>
    </body>
</html>
