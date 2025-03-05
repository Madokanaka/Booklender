<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Список сотрудников</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">
    <h1 class="text-center mb-4">Сотрудники</h1>
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
        <#list employees as employee>
            <div class="col">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">${employee.name} - ${employee.position}</h5>
                        <ul class="list-unstyled">
                            <li><strong>Текущие книги:</strong></li>
                            <#list employee.currentBooks as bookId>
                                <#assign book = books[bookId-1]>
                                <li>${book.title}</li>
                            </#list>
                        </ul>
                        <a href="/employee?employeeId=${employee.id}" class="btn btn-primary mt-3">Подробнее</a>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
