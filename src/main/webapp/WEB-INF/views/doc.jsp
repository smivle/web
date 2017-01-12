<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN""http://www.w3.org/TR/html4/loose.dtd"
><html ng-app="app">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/x-mathjax-config">
        MathJax.Hub.Config({tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}});
    </script>
    <script type="text/javascript" async
      src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS_CHTML">
    </script>
    <title>STAGIRS</title>
    <script src="static/jquery/jquery.js"></script>
    <link rel="stylesheet" href="static/awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="static/bootstrap-select.min.css">
    <script src="static/bootstrap-select.min.js"></script>
    <link rel="stylesheet" href="static/bootstrap.min.css">
    <script src="static/jquery.min.js"></script>
    <script src="static/bootstrap.min.js"></script>
    <script src="static/overlay/loadingoverlay.js"></script>
    <script src="static/angular.min.js"></script>
    <link href="static/nouislider/nouislider.min.css" rel="stylesheet">
    <script src="static/nouislider/nouislider.min.js"></script>
    <style>
        body{
            background-color: #f1f1f1
        }
        .body{
            width:1024px;
            margin: 20px auto;
            font-size: 20px;
            font-family: serif;
            border-radius: 5px;
            background-color: #fff;
            padding: 20px
        }
        .meta{
            width:1024px;
            margin: 20px auto;
            text-align: center; 
        }
        p {
            text-indent: 40px; 
            text-align: left;
        }
        .title{
            font-size: 25px;
            font-family: serif;
            font-weight: bold;
        }
        .thanks{
            font-size: 15px;
            font-style: italic;
            font-family: serif;
            margin: 20px;
        }
        .author{
            font-size: 20px;
            font-family: serif;
            font-weight: bold;
            margin: 20px;
        }
        .info{
            font-size: 15px;
            font-style: italic;
            font-family: serif;
            margin: 20px;
        }
        .keyword{
            text-decoration: underline;
            font-weight: bold;    
        }
        i{
            text-indent: 0px;
        }
    </style>
    <script>
        var scope;
        (function() {

            'use strict';

            angular
                .module('app', [])
                .config( [
                    '$compileProvider',
                    function( $compileProvider )
                    {   
                        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension|consultantplus):/);
                        // Angular before v1.2 uses $compileProvider.urlSanitizationWhitelist(...)
                    }
                ])
                .controller('AppController', ['$scope', '$http', function($scope, $http) {
                    scope = $scope;
                    $scope.text = '${query}';
                    $scope.docId = '${doc.id}';
                    $scope.maxSemantic = 0;
                    $.each($(".sentence"), function(k, v){
                        $scope.maxSemantic = Math.max($scope.maxSemantic, parseFloat($(v).attr("semantic")));
                    });    
                    $scope.minShow = 0;
                    $scope.maxShow = 100;
                    $scope.filter = function(){
                        $scope.minShow = parseInt($("#minShow").val());
                        $scope.maxShow = parseInt($("#maxShow").val());
                    }    
                    $scope.update = function(){
                        window.location = "doc?docId=" + $scope.docId + "&query=" + $scope.text;
                    };
                }]);
        })();
        $(document).ready(function(){

            noUiSlider.create($("#showrate-filter")[0], {
                start: [ 0, 100], // 4 handles, starting at...
                connect: true, // Display a colored bar between the handles
                step: 1,
                //tooltips: true,
                range: {
                    'min': 0,
                    'max': 100
                }
            });
            $("#showrate-filter")[0].noUiSlider.on('update', function ( values, handle ) {
                $( "#showrate-value" ).text( parseFloat(values[0]).toFixed(0) + " - " + parseFloat(values[1]).toFixed(0) );
                if(handle == 0){
                    $("#minShow").val(parseFloat(values[0]));
                }else{
                    $("#maxShow").val(parseFloat(values[1]));
                }  
                $( "#showrate-value" ).click();
            });
            $('.selectpicker').change(function(){
                $("#search-button").click();
            });
            $("#query").keypress(function (e) {
                if (e.which == 13) {
                  $("#search-button").click();
                  return false;    //<---- Add this line
                }
              });
            
            $( "i" ).tooltip();
        });
    </script>
</head>
<body ng-controller="AppController">
    <div class="meta">
        <div class="title">${doc.title}</div>
        <div class="thanks">${doc.thanks.replaceAll('<.*?>', '')}</div>
        <div class="author">${doc.author}</div>
        <div class="info">${doc.classifier}; ${doc.output}</div>
        <input id="minShow" type="text" ng-model="minShow" style="display: none">
        <input id="maxShow" type="text" ng-model="maxShow" style="display: none">
        
        <div style="display: inline-block; vertical-align: top; width:450px;">
            <label>Фильтр по тексту предложения:</label>   
            <div>
                <input id="query" type="text" class="form-control" style="width:400px;margin-right: 5px;display: inline-block;" ng-model="text">
                <button ng-click="update()" id="search-button" type="button" class="btn btn-info"><i class="fa fa-search" data-original-title="" title=""></i></button>
            </div>
        </div>
        <div style="display: inline-block; vertical-align: top; width:220px;margin-left: 40px">
            <label>Информативность (%): <span id="showrate-value" ng-click="filter()"></span></label>   
            <div id="showrate-filter" style="margin-top: 10px;"></div> 
        </div>
    </div>
    <div class="body">
        <c:forEach items="${doc.blocks}" var="block">
            <c:if test="${block.isPoint()}">
                <c:set var="point" value="${block}" scope="request" />
                <jsp:include page="block/point.jsp" />
            </c:if>
            <c:if test="${block.isSection()}">
                <c:set var="section" value="${block}" scope="request" />
                <jsp:include page="block/section.jsp" />
            </c:if>
        </c:forEach>  
    </div>
</body>
</html>
