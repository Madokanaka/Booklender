<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Список сотрудников</title>
    <link rel="stylesheet" href="/css/styleOfPages.css">
</head>
<body>
<div class="container">
    <h1>Сотрудники</h1>
    <ul class="employee-list">
        <#list employees as employee>
            <li class="employee-item">
                <a href="/employee/${employee.id}" class="employee-link">${employee.name} - ${employee.position}</a>
                <ul class="book-list">
                    <li><strong>Текущие книги:</strong></li>
                    <#list employee.currentBooks as bookId>
                        <#assign book = books[bookId-1]>
                        <li class="book-item">${book.title}</li>
                    </#list>
                </ul>
            </li>
        </#list>
    </ul>
</div>
</body>
</html>
