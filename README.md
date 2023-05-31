## Функциональность:
* Создание простых задач - задача имеет название, описание, время начала 
и продолжительность, время окончания (рассчитывается автоматически);
* Создание задач, содержащих в себе подзадачи (Эпик);
* Эпик так же имеет название и описание, время начала, продолжительность и окончание рассчитывается на основе подзадач, входящих в него;
* Подзадача эпика может быть создана только на основе существующей задачи;
* При обращении к задаче, трекер сохраняет ее в историю просмотров;
* Реализована возможность вывода задач в порядке приоритета (первая - самая ранняя);
* Трекер хранит в отдельном файле, расположенном в директории resources/task.csv, информацию обо всех созданных задачах,
  а так же об истории просмотров, с возможностью
  дальнейшего восстановления;
* Все задачи, подзадачи и эпики имеют статус NEW, IN_PROGRESS или DONE. В зависимости от статуса подзадач, входящих в
  эпик, определяется статус самого эпика;
* Задачу можно обновить, автоматически обновится статус эпика, если данная задача входит в эпик.