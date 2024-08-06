<%-- 
    Document   : register
    Created on : Jul 2, 2024, 8:21:34 PM
    Author     : saleh
--%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="helpers.HttpHelper"%>  
<%@page import="helpers.ContentCreator" %>
<%@page import="middlewares.RedirectAfterLogin"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <jsp:include page="includes/style.jsp"/> 
        <script>
            function toggleRoleFields() {
                const role = document.querySelector('input[name="role"]:checked').value;
                document.getElementById('customerFields').style.display = (role === 'Customer') ? 'block' : 'none';
                document.getElementById('jobseekerFields').style.display = (role === 'Jobseeker') ? 'block' : 'none';
            }

            window.onload = function () {
                toggleRoleFields(); 
            }
        </script>
    </head>
    <%
        out.print(NotificationHelper.displayNotifications(request));
    %>
    <body class="bg-gray-100">
        <div class="container mx-auto p-4">
            <div class="max-w-md mx-auto bg-white p-6 rounded-lg shadow-lg">
                <h2 class="text-2xl font-bold mb-6 text-center">Register</h2>
                <form method="POST" action="Register" class="space-y-4">
                    <div>
                        <label for="name" class="block text-gray-700 text-sm font-bold mb-2">Name</label>
                        <input type="text" name="name" id="name" placeholder="Name" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div>
                        <label for="username" class="block text-gray-700 text-sm font-bold mb-2">Username</label>
                        <input type="text" name="username" id="username" placeholder="Username" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div>
                        <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password</label>
                        <input type="password" name="password" id="password" placeholder="Password" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div>
                        <label for="confirmPassword" class="block text-gray-700 text-sm font-bold mb-2">Confirm Password</label>
                        <input type="password" name="confirmPassword" id="confirmPassword" placeholder="Confirm Password" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div>
                        <label for="email" class="block text-gray-700 text-sm font-bold mb-2">Email</label>
                        <input type="email" name="email" id="email" placeholder="Email" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div>
                        <label for="phoneNumber" class="block text-gray-700 text-sm font-bold mb-2">Phone Number</label>
                        <input type="text" name="phoneNumber" id="phoneNumber" placeholder="Phone Number" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>
                    <div>
                        <span class="text-left text-sm font-medium text-gray-700 mt-4 block">Role</span>
                        <label class="inline-flex items-center mt-3">
                            <input type="radio" class="form-radio h-5 w-5 text-gray-600" name="role" value="Customer" onchange="toggleRoleFields();" required>
                            <span class="ml-2 text-gray-700">Customer</span>
                        </label>
                        <label class="inline-flex items-center mt-3 ml-4">
                            <input type="radio" class="form-radio h-5 w-5 text-gray-600" name="role" value="Jobseeker" onchange="toggleRoleFields();" required>
                            <span class="ml-2 text-gray-700">Jobseeker</span>
                        </label>
                    </div>
                    <div id="customerFields" class="hidden">
                        <div class="mt-4">
                            <label for="customerAddress" class="block text-gray-700 text-sm font-bold mb-2">Address</label>
                            <input type="text" name="customerAddress" id="customerAddress" placeholder="Address" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>
                        <div class="mt-4">
                            <label for="companyName" class="block text-gray-700 text-sm font-bold mb-2">Company Name</label>
                            <input type="text" name="companyName" id="companyName" placeholder="Company Name" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>
                        <div class="mt-4">
                            <label for="website" class="block text-gray-700 text-sm font-bold mb-2">Website</label>
                            <input type="text" name="website" id="website" placeholder="Website" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>
                    </div>
                    <div id="jobseekerFields" class="hidden">
                        <div class="mt-4">
                            <label for="age" class="block text-gray-700 text-sm font-bold mb-2">Age</label>
                            <input type="number" name="age" id="age" placeholder="Age" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>
                        <div class="mt-4">
                            <label for="jobseekerAddress" class="block text-gray-700 text-sm font-bold mb-2">Address</label>
                            <input type="text" name="jobseekerAddress" id="jobseekerAddress" placeholder="Address" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>
                        <div class="mt-4">
                            <label for="hobbies" class="block text-gray-700 text-sm font-bold mb-2">Hobbies</label>
                            <input type="text" name="hobbies" id="hobbies" placeholder="Hobbies" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>
                        <div class="mt-4">
                            <span class="block text-gray-700 text-sm font-bold mb-2">Gender</span>
                            <label class="inline-flex items-center mr-4">
                                <input type="radio" name="gender" value="Male" class="form-radio h-5 w-5 text-blue-600">
                                <span class="ml-2 text-gray-700">Male</span>
                            </label>
                            <label class="inline-flex items-center">
                                <input type="radio" name="gender" value="Female" class="form-radio h-5 w-5 text-blue-600">
                                <span class="ml-2 text-gray-700">Female</span>
                            </label>
                        </div>
                        <div class="mt-4">
                            <label for="skills" class="block text-gray-700 text-sm font-bold mb-2">Skills</label>
                            <input type="text" name="skills" id="skills" placeholder="Skills" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                    </div>
                    <div class="mt-6">
                        <button type="submit" class="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Register</button>
                    </div>
                </form>
                <div class="text-center mt-4">
                    <a href= "<%=new Route().add(BaseRoute.CONTEXT.getPath()).add(JspFile.LOGIN.getPath()).get()%>" class="text-blue-500 hover:underline">Already have an account? Login now!</a>
                </div>
            </div>
        </div>
    </body>
</html>
