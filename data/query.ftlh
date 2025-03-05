<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
          name="viewport">
    <meta content="ie=edge" http-equiv="X-UA-Compatible">
    <title>Query parameters sample page</title>
</head>
<body>
    <div style="margin-left: 20px;">
        <h1>Query parameters sample page</h1>

        <#if params??>
            <ul>
                <#list params as name, value>
                    <li> <b>${name}</b> - <span style="background-color: lightgray;">${value}</span></li>
                </#list>
            </ul>
        </#if>
    </div>
</body>
</html>