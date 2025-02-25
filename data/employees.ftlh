<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Список сотрудников</title>
</head>
<body>
<h1>Сотрудники</h1>
<ul>
    <#list employees as employee>
        <li>
            ${employee.name} - ${employee.position}
            <ul>
                <li><strong>Текущие книги:</strong>
                    <#list employee.currentBooks as bookId>
                        <#assign book = books[bookId-1]>
                        <div>${book.title}</div>
                    </#list>
                </li>
            </ul>
        </li>
    </#list>
</ul>
</body>
</html>
