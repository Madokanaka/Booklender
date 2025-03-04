<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
          name="viewport">
    <meta content="ie=edge" http-equiv="X-UA-Compatible">
    <title>Cookies Sample Page</title>
</head>

<body>
<div style="margin-left: 20px;">
    <h1> Visited ${times} <#if times==1>time<#else>times</#if>
    </h1>

    <#if globalVisit??>
        <h2> Global visit counter ${globalVisit} <#if globalVisit==1>time<#else>times</#if> (until server restart)
        </h2>
    </#if>

    <#if cookies??>
        <ul>
            <#list cookies as name, value>
                <li><b>${name}</b> - <span style="background-color: lightgray;">${value}</span></li>
            </#list>
        </ul>
    </#if>

</div>
</body>

</html>