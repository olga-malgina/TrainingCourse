<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>REQUESTS PAGE</title>

        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
            <script>
                $(document).on("click", "#getbutton", function() {
                    $.get("servlet", function(responseText) {
                        $("#response").text(responseText);
                    });
                });
            </script>

            <script>
                $(document).on("click", "#postbutton", function() {
                    $.post("servlet", function(responseText) {
                        $("#response").text(responseText);
                    });
                });
            </script>

            <script>
                $(document).on("click", "#putbutton", function() {
                    $.ajax({
                        url: '\servlet',
                        type: 'PUT',
                        success: function(responseText) {
                            $("#response").text(responseText);
                        }
                    });
                });
            </script>

            <script>
                $(document).on("click", "#deletebutton", function() {
                    $.ajax({
                        url: '\servlet',
                        type: 'DELETE',
                        success: function(responseText) {
                           $("#response").text(responseText);
                        }
                    });
                });
            </script>
    </head>

    <body>
    <center>
        <h1>REQUESTS PAGE<br><br></h1>


        <button id="getbutton">GET</button>
        <br><br>

        <button id="postbutton">POST</button>
        <br><br>

        <button id="putbutton">PUT</button>
        <br><br>

        <button id="deletebutton">DELETE</button>
        <br><br>


        <div id="response"></div>
        <br><br>

        <p>Number of clicks: ${cookie['visits'].getValue()}</p>


    </body>
</html>
