Запускать тест filterAndChooseCourse()
В нем определены для примера некоторые параметры 
параметры браузера(фулл-скрин,  отключение уведомлений).
Метод filterCourse("C#") выполняет поиск курсов 
на странице Программирование,по фильтру который 
прописывается во входном параметре метода,
в примере происходит отбор курсов, содержащих 
слово  "С#"
Метод chooseCourse() сначала собирает все даты начала курсов
на вкладке программирование в массив, далеераспарсивает
эти даты,фильтрует, выполняется поиск самой ранней
и самой поздней даты.Затем эти две даты обратно 
распарсиваются в соответствии с паттерном на странице
Программирование и по локаторым находятся соответствующие 
им названия курсов и поочередный клик по ним. Процесс 
обработки дат полностью логирую для наглядности. 

По замечаниям:
1. Убрала  throws ParseException
2. Добавила стрим в код

Также добавила подсветку при выборе курсов
⬇️⬇️⬇️

📚Домашнее задание / проектная работа разработано (-на) для курса " https://otus.ru/lessons/java-qa-pro/ "
