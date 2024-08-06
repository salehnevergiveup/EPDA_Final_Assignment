/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import controllers.enums.BaseRoute;
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author saleh
 */
import java.util.List;
import java.util.Map;
import model.Model;

public class ContentCreator {

    private String content = "";

    public enum Wrapper {
        CONTAINER, DIV, FORM
    }

    public String addStyle(String style) {
        return style.isEmpty() ? "" : " style='" + style + "'";
    }

    public String addClasses(String cla) {
        return cla.isEmpty() ? "" : " class='" + cla + "'";
    }

    public ContentCreator start(Wrapper wrapper, String classes, String style) {
        classes = classes.isEmpty() ? "" : " " + classes;
        style = style.isEmpty() ? "" : " style='" + style + "'";

        switch (wrapper) {
            case CONTAINER:
                this.content = this.content + "<div class='px-4 container lg:w-3/5 mx-auto" + classes + "'" + style + ">";
                break;
            case DIV:
                this.content = this.content + "<div class='" + classes + "'" + style + ">";
                break;
            case FORM:
                this.content = this.content + "<form class='" + classes + "'" + style + ">";
                break;
        }
        return this;
    }

    public ContentCreator end(Wrapper wrapper) {
        switch (wrapper) {
            case FORM:
                this.content += "</form>";
                break;
            default:
                this.content += "</div>";
        }
        return this;
    }

    public ContentCreator input(String name, String type, String value, String placeHolder, boolean required, String classes, String style) {
        String isRequired = required ? " required" : "";
        classes = classes.isEmpty() ? "border rounded p-2 w-full" : classes;

        String placeholderAttribute = value.isEmpty() ? " placeholder='" + placeHolder + "'" : "";
        String valueAttribute = value.isEmpty() ? "" : " value='" + value + "'";

        this.content = this.content + "<input type='" + type + "' name='" + name + "'" + isRequired + placeholderAttribute + valueAttribute;
        this.content += this.addClasses(classes);
        this.content += this.addStyle(style);
        this.content += "/>";
        return this;
    }

    public ContentCreator paragraph(String text, String classes, String style) {
        classes = classes.isEmpty() ? "text-base mb-4" : classes;
        this.content = this.content + "<p";
        this.content += this.addClasses(classes);
        this.content += this.addStyle(style);
        this.content += ">" + text + "</p>";
        return this;
    }

    public ContentCreator condationalparagraph(String text, String role, String userRole, String classes, String style) {
        if (role.equalsIgnoreCase(userRole)) {
            classes = classes.isEmpty() ? "text-base mb-4" : classes;
            this.content = this.content + "<p";
            this.content += this.addClasses(classes);
            this.content += this.addStyle(style);
            this.content += ">" + text + "</p>";
        }
        return this;
    }

    public ContentCreator condationalinput(String name, String type, String value, String placeHolder, boolean required, String role, String userRole, String classes, String style) {
        if (role.equalsIgnoreCase(userRole)) {
            String isRequired = required ? " required" : "";

            classes = classes.isEmpty() ? "border rounded p-2 w-full" : classes;

            String placeholderAttribute = value.isEmpty() ? " placeholder='" + placeHolder + "'" : "";
            String valueAttribute = value.isEmpty() ? "" : " value='" + value + "'";

            this.content = this.content + "<input type='" + type + "' name='" + name + "'" + isRequired + placeholderAttribute + valueAttribute;
            this.content += this.addClasses(classes);
            this.content += this.addStyle(style);
            this.content += "/>";
        }
        return this;
    }

    public ContentCreator header(String text, int size, String classes, String style) {
        int validSize = (size > 0 && size < 7) ? size : 1;
        classes = classes.isEmpty() ? "text-center text-2xl text-gray-600 font-bold mb-4" : classes;
        this.content = this.content + "<h" + validSize;
        this.content += this.addClasses(classes);
        this.content += this.addStyle(style);
        this.content += ">" + text + "</h" + validSize + ">";
        return this;
    }

    public ContentCreator textArea(String name, String text, String classes, String style) {
        classes = classes.isEmpty() ? "border rounded p-2 w-full h-32" : classes;
        this.content = this.content + "<textarea name='" + name + "'";
        this.content += this.addClasses(classes);
        this.content += this.addStyle(style);
        this.content += ">" + text + "</textarea>";
        return this;
    }

    public ContentCreator submit(String name, String classes, String style) {
        classes = classes.isEmpty() ? "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" : classes;
        this.content = this.content + "<input type='submit' value='" + name + "'";
        this.content += this.addClasses(classes);
        this.content += this.addStyle(style);
        this.content += "/>";
        return this;
    }

    public ContentCreator action(String name, String link, String role, String accessRole, String classes, String style) {
        classes = classes.isEmpty() ? "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" : classes;
        if (role.equalsIgnoreCase(accessRole) || accessRole.equalsIgnoreCase("Admin")) {
            this.content = this.content + "<a href='" + link + "'";
            this.content += this.addClasses(classes);
            this.content += this.addStyle(style);
            this.content += "><button>" + name + "</button></a>";
        }
        return this;
    }

    public ContentCreator link(String text, String href, String classes, String style, boolean isBlock) {
        classes = classes.isEmpty() ? "text-blue-500 hover:underline" : classes;
        String display = isBlock ? "block" : "inline";
        this.content = this.content + "<a href='" + href + "'" + this.addClasses(classes) + this.addStyle(style) + " style='display: " + display + "'>" + text + "</a>";
        return this;
    }

    public String tableAction(String content, String link, String role, String accessRole, String classes, String style) {
        String component = "";
        classes = classes.isEmpty() ? "gray-100 hover:text-blue-700" : classes;
        if (role.equalsIgnoreCase(accessRole) || accessRole.equalsIgnoreCase("Admin")) {
            component = "<a href='" + link + "'";
            component += this.addClasses(classes);
            component += this.addStyle(style);
            component += ">";
            component += content;
            component += "</a>";
        }
        return component;
    }

    public ContentCreator form(String method, String action, String classes, String style) {
        classes = classes.isEmpty() ? "space-y-4" : classes;
        this.content = this.content + "<form method='" + method + "' action='" + action + "'";
        this.content += this.addClasses(classes);
        this.content += this.addStyle(style);
        this.content += ">";
        return this;
    }

    public ContentCreator table(String title, List<String> headers, List<Map<String, String>> details, List<List<String>> actionData, String classes, String style) {
        classes = classes.isEmpty() ? "min-w-full leading-normal overflow-y: auto" : classes;
        this.content += "<div><h3 class='text-2xl font-semibold leading-tight mb-2 text-gray-600'>" + title + "</h3></div>";
        this.content += "<div overflow-y: auto;' class='" + classes + "'>";
        this.content += "<table style='width: 100%;' class='full overflow-y: auto '";
        this.content += this.addClasses(classes);
        this.content += this.addStyle(style);
        this.content += ">";
        this.content += "<thead class='bg-gray-100'><tr>";
        for (String header : headers) {
            this.content += "<th scope='col' class='px-5 py-3 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider'>" + header + "</th>";
        }
        System.out.println("table 1.5");
        this.content += "</tr></thead>";
        this.content += "<tbody class='bg-white'>";
        System.out.println("details: " + details);
        details.forEach(data -> {
            int id = Integer.parseInt(data.get("id"));
            this.content += "<tr>";
            headers.forEach(head -> {
                if (!head.equalsIgnoreCase("Actions") && data.containsKey(head)) {
                    this.content += "<td class='px-2 py-2 border-b border-gray-200 text-sm'>";
                    this.content += "<div class='flex'>";
                    this.content += "<div class='ml-3'><p class='text-gray-900 whitespace-no-wrap'>" + data.get(head) + "</p></div>";
                    this.content += "</div>";
                    this.content += "</td>";
                }
            });

            this.content += "<td class='px-2 py-2 border-b border-gray-200 text-sm text-right' x-data='{ open: false }'>";
            this.content += "<div class='relative'>";
            this.content += "<button @click='open = !open' class='text-gray-500 hover:text-gray-700'>";
            this.content += "<svg class='inline-block h-6 w-6 fill-current' viewBox='0 0 24 24'>";
            this.content += "<path d='M12 6a2 2 0 110-4 2 2 0 010 4zm0 8a2 2 0 110-4 2 2 0 010 4zm-2 6a2 2 0 104 0 2 2 0 00-4 0z' />";
            this.content += "</svg>";
            this.content += "</button>";
            this.content += "<div x-show='open' @click.away='open = false' class='absolute right-0 mt-2 w-48 bg-white border border-gray-200 rounded shadow-lg py-1 z-20'>";
            actionData.forEach(action -> {
                String entity = action.get(0).trim();
                String content = action.get(1).trim();
                String link = action.get(2).trim();
                String role = action.get(3).trim();
                String accessRole = action.get(4).trim();
                boolean withId = Boolean.parseBoolean(action.get(5).trim());
                String actionClasses = action.get(6).trim();
                String actionStyle = action.get(7).trim();

                if (data.containsKey(entity)) {
                    this.content += "<div class='block px-2 text-sm text-gray-700 hover:bg-gray-100 flex flex-start'>"
                            + this.tableAction(content, (link.contains("?") ? link + "&id=" : link + "?id=") + data.get(entity), role, accessRole, actionClasses, actionStyle)
                            + "</div>";
                } else {
                    this.content += "<div class='block px-2 text-sm text-gray-700 hover:bg-gray-100 flex flex-start'>"
                            + this.tableAction(content, (link.contains("?") ? link + "&id=" : link + "?id=") + id, role, accessRole, actionClasses, actionStyle)
                            + "</div>";
                }

            });
            this.content += "</div>";
            this.content += "</div>";
            this.content += "</td>";
            this.content += "</tr>";

        });

        this.content += "</tbody>";

        this.content += "</table> </div>";
        return this;
    }

    public ContentCreator label(String contentText, String classes, String style, String attributes) {
        classes = classes.isEmpty() ? "block text-gray-700 text-sm font-bold mb-2" : classes;
        this.content += "<label " + attributes + " ";
        this.content += this.addClasses(classes);
        this.content += this.addStyle(style);
        this.content += ">" + contentText + "</label>";
        return this;
    }

    public ContentCreator select(String name, List<String> options, String classes, String style, String attributes) {
        classes = classes.isEmpty() ? "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" : classes;
        this.content += "<select name='" + name + "' ";
        this.content += this.addClasses(classes);
        this.content += this.addStyle(style);
        this.content += attributes + ">";
        for (String option : options) {
            this.content += "<option value='" + option + "'>" + option + "</option>";
        }
        this.content += "</select>";
        return this;
    }

    public ContentCreator selectList(String name, List<List<String>> options, String classes, String style, String role, String accessRole) {
        if (role.equalsIgnoreCase(accessRole) || accessRole.equalsIgnoreCase("Admin")) {
            System.out.println("he can seed it");
            classes = classes.isEmpty() ? "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" : classes;
            this.content += "<select name='" + name + "' ";
            this.content += this.addClasses(classes);
            this.content += this.addStyle(style);
            for (List<String> option : options) {
                this.content += "<option value='" + option.get(0) + "'>" + option.get(1) + "</option>";
            }
            this.content += "</select>";
        }
        return this;
    }

    public ContentCreator deleteButtonWithPopup(String buttonText, String deleteUrl, String role, String accessRole, String classes, String style) {
        if (role.equalsIgnoreCase(accessRole) || accessRole.equalsIgnoreCase("Admin")) {
            classes = classes.isEmpty() ? "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" : classes;
            this.content += "<button onclick=\"showDeleteModal('" + deleteUrl + "')\" ";
            this.content += this.addClasses(classes);
            this.content += this.addStyle(style);
            this.content += ">" + buttonText + "</button>";
            this.content += "<div id='deleteModal' class='fixed inset-0 flex items-center justify-center z-50 bg-gray-900 bg-opacity-50' style='display: none;'>";
            this.content += "<div class='bg-white rounded-lg shadow-lg p-6 w-96'>";
            this.content += "<div class='modal-header flex justify-between items-center'>";
            this.content += "<h5 class='text-xl font-semibold'>Delete Confirmation</h5>";
            this.content += "<button onclick=\"hideDeleteModal()\" class='text-gray-400 hover:text-gray-600'>&times;</button>";
            this.content += "</div>";
            this.content += "<div class='modal-body mt-4'><p>Are you sure you want to delete this item?</p></div>";
            this.content += "<div class='modal-footer mt-6 flex justify-end space-x-2'>";
            this.content += "<button onclick=\"hideDeleteModal()\" class='bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded'>Cancel</button>";
            this.content += "<a id='confirmDeleteButton' href='' class='bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded'>Delete</a>";
            this.content += "</div></div></div>";
            this.content += "<script>";
            this.content += "function showDeleteModal(url) { document.getElementById('deleteModal').style.display = 'flex'; document.getElementById('confirmDeleteButton').href = url; }";
            this.content += "function hideDeleteModal() { document.getElementById('deleteModal').style.display = 'none'; }";
            this.content += "</script>";
        }
        return this;
    }

    public ContentCreator warningButtonWithPopup(String buttonText, String warningUrl, String role, String accessRole, String classes, String style) {
        if (role.equalsIgnoreCase(accessRole) || accessRole.equalsIgnoreCase("Admin")) {
            classes = classes.isEmpty() ? "bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded" : classes;
            this.content += "<button onclick=\"showWarningModal('" + warningUrl + "')\" ";
            this.content += this.addClasses(classes);
            this.content += this.addStyle(style);
            this.content += ">" + buttonText + "</button>";
            this.content += "<div id='warningModal' class='fixed inset-0 flex items-center justify-center z-50 bg-gray-900 bg-opacity-50' style='display: none;'>";
            this.content += "<div class='bg-white rounded-lg shadow-lg p-6 w-96'>";
            this.content += "<div class='modal-header flex justify-between items-center'>";
            this.content += "<h5 class='text-xl font-semibold'>Warning</h5>";
            this.content += "<button onclick=\"hideWarningModal()\" class='text-gray-400 hover:text-gray-600'>&times;</button>";
            this.content += "</div>";
            this.content += "<div class='modal-body mt-4'><p>Are you sure you want to proceed with this action?</p></div>";
            this.content += "<div class='modal-footer mt-6 flex justify-end space-x-2'>";
            this.content += "<button onclick=\"hideWarningModal()\" class='bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded'>Cancel</button>";
            this.content += "<a id='confirmWarningButton' href='' class='bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded'>Proceed</a>";
            this.content += "</div></div></div>";
            this.content += "<script>";
            this.content += "function showWarningModal(url) { document.getElementById('warningModal').style.display = 'flex'; document.getElementById('confirmWarningButton').href = url; }";
            this.content += "function hideWarningModal() { document.getElementById('warningModal').style.display = 'none'; }";
            this.content += "</script>";
        }
        return this;
    }

    public ContentCreator space(int space) {
        if (space == 0) {
            this.content += "<div class='my-4'></div>";
        } else {
            this.content += "<div style='margin-top: " + space + "px; margin-bottom: " + space + "px;'></div>";
        }
        return this;
    }

    public ContentCreator popupComment(String entityId, String applicationId) {
        this.content += "<div id='popupModal' x-data='{ show: true, rating: 0 }' x-show='show' class='fixed inset-0 flex items-center justify-center z-50 bg-gray-900 bg-opacity-50'>";
        this.content += "<div class='max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg overflow-hidden mb-8'>";
        this.content += "<div class='flex justify-between items-center mb-6'>";
        this.content += "<h2 class='text-2xl text-gray-600 font-bold'>Comment</h2>";
        this.content += "<button @click='show = false' class='text-gray-600 hover:text-gray-900'>&times;</button>";
        this.content += "</div>";
        this.content += "<form method='POST' action='" + new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.COMMENTS.getPath()).add(ServletFile.CREATE.getPath()).get() + "' class='space-y-4' id='reviewForm'>";
        this.content += "<input type='hidden' name='rating' x-model='rating'>";
        this.content += "<input type='hidden' name='application' value='" + applicationId + "'>";
        this.content += "<input type='hidden' name='id' value='" + entityId + "'>";
        this.content += "<div class='flex mb-4 justify-center'>";
        this.content += "<template x-for='star in [1, 2, 3, 4, 5]' :key='star'>";
        this.content += "<i @click='rating = star' :class=\"{'text-yellow-500': rating >= star, 'text-gray-400': rating < star }\" class='fas fa-star text-2xl cursor-pointer mx-1'></i>";
        this.content += "</template>";
        this.content += "</div>";
        this.content += "<textarea name='commentContent' placeholder='Please enter your review...' required class='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'></textarea>";
        this.content += "<button type='submit' class='w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'>Submit</button>";
        this.content += "<button type='button' @click='show = false' class='w-full bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded mt-2'>Close</button>";
        this.content += "</form>";
        this.content += "</div>";
        this.content += "</div>";
        return this;
    }

    public ContentCreator notificationPopup(String title, String message, String icon, String color) {
        this.content += "<div x-data='{ show: true }' x-show='show' class='fixed inset-0 flex items-center justify-center z-50 bg-gray-900 bg-opacity-50'>";
        this.content += "<div class='bg-white rounded-lg shadow-lg p-6 w-96'>";
        this.content += "<div class='modal-header flex justify-between items-center mb-4'>";
        this.content += "<div class='text-3xl mr-2'>" + icon + "</div>";
        this.content += "<h5 class='text-xl font-semibold'>" + title + "</h5>";
        this.content += "<button @click='show = false' class='text-" + color + "-400 hover:text-" + color + "-600'>&times;</button>";
        this.content += "</div>";
        this.content += "<div class='modal-body mt-4'><p class='text-center'>" + message + "</p></div>";
        this.content += "<div class='modal-footer mt-6 flex justify-center'>";
        this.content += "<button @click='show = false' class='bg-" + color + "-500 hover:bg-" + color + "-700 text-white font-bold py-2 px-4 rounded'>Close</button>";
        this.content += "</div></div></div>";
        return this;
    }
    
     public ContentCreator radio(String name, String selectedValue, String role, String userRole, String classes, String style) { 
        if(role.equalsIgnoreCase(userRole)) { 
            classes = classes.isEmpty() ? "form-radio h-5 w-5 text-blue-600" : classes;
            // Male radio button
            String checkedMale = "Male".equalsIgnoreCase(selectedValue) ? " checked" : "";
            this.content += "<label class='inline-flex items-center mr-4'>";
            this.content += "<input type='radio' name='" + name + "' value='Male'" + checkedMale + this.addClasses(classes) + this.addStyle(style) + ">";
            this.content += "<span class='ml-2 text-gray-700'>Male</span>";
            this.content += "</label>";
            // Female radio button
            String checkedFemale = "Female".equalsIgnoreCase(selectedValue) ? " checked" : "";
            this.content += "<label class='inline-flex items-center'>";
            this.content += "<input type='radio' name='" + name + "' value='Female'" + checkedFemale + this.addClasses(classes) + this.addStyle(style) + ">";
            this.content += "<span class='ml-2 text-gray-700'>Female</span>";
            this.content += "</label>"; 
        }
        return this;
    }


    public String getContent() {
        return content;
    }
}
