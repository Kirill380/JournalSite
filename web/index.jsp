<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <!--<meta charset="UTF-8">-->
    <link href="/resources/css/bootstrapv3.css" rel="stylesheet" type="text/css">
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="/resources/css/journal.css" rel="stylesheet" type="text/css">
    <script src="/resources/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="/resources/js/bootstrapv3.js" type="text/javascript"></script>
    <script src="/resources/js/bootstrap-paginator.js" type="text/javascript"></script>

    <title></title>
</head>

<body>
<div class="page-header">
    <h1>Journal viewer</h1>
</div>

<br>

<div class="">
    <form id="option" class="form-inline" action="/content" method="post">
        <label class="control-label" for="size">Choose size: </label>
        <select class="form-control" id="size" name="size">
            <option>5</option>
            <option>10</option>
            <option>15</option>
            <option>20</option>
        </select>

        <label class="control-label" for="sort">Sort: </label>
        <select class="form-control" id="sort" name="sort">
            <option value="date">Sort by date</option>
            <option value="level">Sort by level</option>
            <option value="source">Sort by source</option>
            <option value="msg">Sort by message</option>
        </select>

        <label class="control-label  col-lg-offset-3" for="from">From</label>
        <fmt:formatDate value="${applicationScope.state.from}" var="from" type="date" pattern="dd-MM-yyyy"/>
        <input type="datetime" class="form-control" value="${from}" id="from" name="from">

        <label class="control-label" for="to">To</label>
        <fmt:formatDate value="${applicationScope.state.to}" var="to" type="date" pattern="dd-MM-yyyy"/>
        <input type="datetime" class="form-control" value="${to}" id="to" name="to">

        <input type="hidden" name="do" value="option">
        <button type="submit" class="btn btn-primary col-lg-offset-1" style="padding-right: 50px; padding-left: 50px;">
            search
        </button>
    </form>
</div>
<br>

<div class="">
    <table class="table table-hover">

        <thead>
        <tr>
            <th class="like-button">#</th>
            <th class="like-button">Date</th>
            <th class="like-button">Level</th>
            <th class="like-button">Source</th>
            <th class="like-button">Message</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="re" items="${requestScope.records}">
            <fmt:formatDate value="${re.date}" var="formattedDate" type="date" pattern="dd-MM-yyyy"/>
            <tr name="${re.id}">
                <td> ${re.id} </td>
                <td>
                    <input type="text" style="border: none" class="form-control edit" id="date"
                           value="${formattedDate}">
                </td>
                <td>
                    <input type="text" style="border: none" class="form-control edit" id="level" value="${re.level}">
                </td>
                <td>
                    <input type="text" style="border: none" class="form-control edit" id="source" value="${re.source}">
                </td>
                <td>
                    <input type="text" class="form-control edit" id="msg" value="${re.msg}">
                </td>
                <td>
                    <div class="form-inline" align="center">
                        <span class="glyphicon glyphicon-trash delete" aria-hidden="true"></span>
                    </div>
                </td>
            </tr>
        </c:forEach>

        </tbody>
        <!---->

    </table>
</div>

<div align="right">
    <button type="button" class="btn btn-success add">Add new</button>
</div>

<div align="center">
    <ul id='bp-3'></ul>
</div>

<script type="text/javascript">


    $(".delete").click(function () {
        //TODO optimize somehow!!
        $.post(
                "/content",
                {do: "delete", id: $(this).parent().parent().parent().attr("name")}
        );

        $(this).parent().parent().parent().remove();
    });

    $(".edit").change(function () {
        var re = /^\d{1,2}-\d{1,2}-\d{4}$/;

        if($(this).attr("id") == "date" && !$(this).val().match(re)) {
            alert("Invalid date format: " + $(this).val());
            return false;
        }
        else {
            $.post(
                    "/content",
                    {
                        do: "update",
                        id: $(this).parent().parent().attr("name"),
                        value: $(this).val(),
                        attr: $(this).attr("id")
                    }
            );
        }
    });

    $(".add").click(function () {
        $.post(
                "/content",
                {do: "add"},
                function (date) {
                    location.reload();
                }
        );
    });

    $("#option").submit(function () {
        var re = /^\d{1,2}-\d{1,2}-\d{4}$/;

        if(!$("#to").val().match(re) || !$("#from").val().match(re)) {
            alert("Invalid date format");
            return false;
        }
        else {
            $.post(
                    "/content",
                    {
                        do: "option",
                        size: $("#size").val(),
                        to: $("#to").val(),
                        from: $("#from").val(),
                        sort: $("#sort").val()
                    },
                    function (data) {
                        location.reload();
                    }
            );
        }
        return false;
    });

    var element = $('#bp-3');

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: ${applicationScope.page},
        size: 'normal',
        numberOfPages: 5,
        totalPages: ${applicationScope.pageNum},
        onPageClicked: function (e, originalEvent, type, page) {
            $.get("/content?page=" + page);
            location.reload();
        }
    }


    element.bootstrapPaginator(options);


</script>

</body>
</html>