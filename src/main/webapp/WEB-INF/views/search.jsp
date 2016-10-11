<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="static/font-awesome/css/font-awesome.min.css">
        <title>Smivle Search System</title>
        <style>
            input.query{
                font-size: 20px;
                padding: 5px;
                max-width: 600px;
                width: 90%;
                border-color: #88a;
            }
            input.query:hover{
                border-color: #335;
            }    
            .query-button{
                color: #335;
                font-size: 30px;
                cursor: pointer;
            }
            .query-button:hover{
                color: #668;
            }    
            .tag-block{
                display: inline-block;
                border-radius: 5px;
                background-color: #fff;
                padding: 10px;
                margin: 10px;
            }
            .tag-block-text{
                width: 500px;
                height: 200px;
                overflow: hidden;
                text-overflow: ellipsis;
                text-align: left
            }
            .result{
                text-align: center;
                margin: 20px;
            }
            .doc-link{
                text-decoration: none;
                color: #335;
            }
        </style>  
        <script src="static/js/jquery-2.0.3.min.js"></script>
        <script>
            $(document).ready(function(){
                $(".query-button").click(function(){
                    window.location = "search?query=" + $(".query").val();
                });
            });
        </script>    
    </head>
    <body style="margin: 0 auto;    background-color: #f1f1f1;">
        <div style="height: 40px; background-color: #335;">
            <a href="search">
                <img style="margin-left: 20px" height="40px" src="static/smivle.png">
            </a>
            <span style="float: right; line-height: 40px;font-size: 30px; margin-right: 20px">
                <a href="search" style="color: #fff;">
                    <i class="fa fa-bars" aria-hidden="true"></i>
                </a>
            </span>    
        </div>
        <div style="text-align: center;margin-top:20px">
            <input class="query" type="text" value="${query}">
            <span class="query-button">
                <i class="fa fa-search" aria-hidden="true"></i>
            </span>    
        </div>
        <div class="result">
            <c:forEach items="${tags}" var="tag">
                <div class="tag-block">
                    <div class="tag-block-text">
                        ${tag.text}
                    </div>
                    <div>
                        <a class="doc-link" href="doc?id=${tag.docHash}&query=${query}">Перейти к документу ...</a>
                    </div>
                </div>
            </c:forEach>    
        </div>    
    </body>
</html>
