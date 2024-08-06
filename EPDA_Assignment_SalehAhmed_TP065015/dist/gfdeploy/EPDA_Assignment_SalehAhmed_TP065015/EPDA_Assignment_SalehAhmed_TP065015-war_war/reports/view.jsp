<%@page import="helpers.NotificationHelper"%>
<%@page import="middlewares.Gate"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="Service.Reports.DTO.ReportsDTO"%>
<%@page import="helpers.HttpHelper"%>
<%@page import="java.util.List, java.util.Map"%>
<!DOCTYPE html>
<html lang="en">
    <%
        ReportsDTO data = (ReportsDTO) request.getAttribute("reportData");
        String reportName = (String) request.getAttribute("reportType");
        System.out.println("ChartLabels: " + data.getChartLabels());
        System.out.println("ChartData: " + data.getChartData());
        System.out.println("SecondaryChartLabels: " + data.getChartLabels());
        System.out.println("SecondaryChartData: " + data.getChartData());
        System.out.println("TableCategory: " + data.getCountByCategory());
        System.out.println("TableDetails: " + data.getDetails());
    %>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><%= reportName%></title>
        <jsp:include page="../includes/style.jsp"/>
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.js"></script>
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/buttons/1.6.5/js/dataTables.buttons.min.js"></script>
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/buttons/1.6.5/js/buttons.html5.min.js"></script>
    </head>
    <body >
        <%Gate.authorise(request, response, "Read Report");%>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <div class="container mx-auto bg-white p-6 rounded-lg shadow-lg">
            <h1 class="text-2xl font-bold mb-4"><%= reportName%></h1>

            <!-- Display Total Count if Available -->
            <% if (data.getTotalCount() != 0) {%>
            <div class="mb-4">
                <h2 class="text-xl font-semibold">Total Count: <%= data.getTotalCount()%></h2>
            </div>
            <% } %>

            <!-- Display Primary Chart if Available -->
            <% if (data.getChartLabels() != null && !data.getChartLabels().isEmpty() && data.getChartData() != null && !data.getChartData().isEmpty()) {%>
            <div class="mb-4">
                <h2 class="text-xl font-semibold"><%= reportName%> Chart</h2>
                <canvas id="primaryChart"></canvas>
                <button id="downloadPrimaryChart" class="mt-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Download Primary Chart as PNG</button>
            </div>
            <% } %>

            <!-- Display Secondary Chart if Available -->
            <% if (data.getSecondaryChartLabels() != null && !data.getSecondaryChartLabels().isEmpty() && data.getSecondaryChartData() != null && !data.getSecondaryChartData().isEmpty()) {%>
            <div class="mb-4">
                <h2 class="text-xl font-semibold"><%= reportName%> Secondary Chart</h2>
                <canvas id="secondaryChart"></canvas>
                <button id="downloadSecondaryChart" class="mt-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Download Secondary Chart as PNG</button>
            </div>
            <% } %>

            <% if (data.getDetails() != null && !data.getDetails().isEmpty()) {%>
            <div class="mt-6">
                <h2 class="text-xl font-semibold"><%= reportName%> Feedback Details</h2>
                <table id="datatable1" class="min-w-full bg-white rounded-lg overflow-hidden">
                    <thead class="bg-blue-500 text-white">
                        <tr>
                            <% for (String header : data.getDetails().get(0).keySet()) {%>
                            <th class="py-2 px-4 border-b-2 border-gray-200 text-left text-xs font-semibold uppercase tracking-wider"><%= header%></th>
                                <% } %>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Map<String, String> row : data.getDetails()) { %>
                        <tr>
                            <% for (String cell : row.values()) {%>
                            <td class="py-2 px-4 border-b border-gray-200"><%= cell%></td>
                            <% } %>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } %>

            <% if (data.getSecondaryDetails() != null && !data.getSecondaryDetails().isEmpty()) {%>
            <div class="mt-6">
                <h2 class="text-xl font-semibold"><%= reportName%> Comment Details</h2>
                <table id="datatable2" class="min-w-full bg-white rounded-lg overflow-hidden">
                    <thead class="bg-red-500 text-white">
                        <tr>
                            <% for (String header : data.getSecondaryDetails().get(0).keySet()) {%>
                            <th class="py-2 px-4 border-b-2 border-gray-200 text-left text-xs font-semibold uppercase tracking-wider"><%= header%></th>
                                <% } %>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Map<String, String> row : data.getSecondaryDetails()) { %>
                        <tr>
                            <% for (String cell : row.values()) {%>
                            <td class="py-2 px-4 border-b border-gray-200"><%= cell%></td>
                            <% } %>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } %>
        </div>

        <script>
            console.log(`<%=(data.getTypeOfChart1())%>`); 
            <% if (data.getChartLabels() != null && !data.getChartLabels().isEmpty() && data.getChartData() != null && !data.getChartData().isEmpty()) { %>
            const ctx = document.getElementById('primaryChart');
            const primaryChart = new Chart(ctx, {
                type: `<%=(data.getTypeOfChart1())%>`,  
                data: {
                    labels: (<%out.println(data.getChartLabels());%>),
                    datasets: [{
                            label:   '<%=reportName%>',
                            data: (<%out.println(data.getChartData());%>),
                            borderWidth: 1,
                            backgroundColor: [
                                'rgb(255, 99, 132)',
                                'rgb(75, 192, 192)',
                                'rgb(255, 205, 86)',
                                'rgb(59, 130, 246)',
                            ]
                        }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });

            document.getElementById('downloadPrimaryChart').addEventListener('click', function() {
                const link = document.createElement('a');
                link.href = primaryChart.toBase64Image();
                link.download = 'primaryChart.png';
                link.click();
            });
            <% }%>

            <% if (data.getSecondaryChartLabels() != null && !data.getSecondaryChartLabels().isEmpty() && data.getSecondaryChartData() != null && !data.getSecondaryChartData().isEmpty()) {%>
            const secondaryCtx = document.getElementById('secondaryChart');
            const secondaryChart = new Chart(secondaryCtx, {
                type: `<%=(data.getTypeOfChart2())%>`,
                data: {
                    labels: <%out.println(data.getSecondaryChartLabels());%>,
                    datasets: [{
                            label: '<%=reportName%>',
                            data: <%out.println(data.getSecondaryChartData());%>,
                            borderWidth: 1,
                            backgroundColor: 'rgba(59, 130, 246,1)',
                            backgroundColor: [
                                'rgb(255, 99, 132)',
                                'rgb(75, 192, 192)',
                                'rgb(255, 205, 86)',
                                'rgb(59, 130, 246)',
                            ]
                        }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });

            document.getElementById('downloadSecondaryChart').addEventListener('click', function() {
                const link = document.createElement('a');
                link.href = secondaryChart.toBase64Image();
                link.download = 'secondaryChart.png';
                link.click();
            });
            <% }%>

              $(document).ready(function () {
        $('#datatable1').DataTable({
            dom: 'Bfrtip',
            buttons: [
                {
                    extend: 'csvHtml5',
                    text: 'Export CSV',
                    className: 'bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'
                }
            ]
        });
        
        $('#datatable2').DataTable({
            dom: 'Bfrtip',
            buttons: [
                {
                    extend: 'csvHtml5',
                    text: 'Export CSV',
                    className: 'bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'
                }
            ]
        });
    });
        </script>
    </body>
</html>
