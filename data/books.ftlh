<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Список книг</title>
</head>
<body>
<h1>Список книг</h1>
<ul>
    <#list books as book>
        <li>
            <a href="/book/${book.id}">${book.title} - ${book.author}</a>
            <#if book.available>
                (Доступна)
            <#else>
                (Выдана)
            </#if>
        </li>
    </#list>
</ul>
</body>
</html>
