package ru.sdmitriy612.gui.localization;

import java.util.ListResourceBundle;

public class Messages_ru_RU extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"appTitle", "Коллекциея квартир"},
                {"userLabel", "Пользователь:"},
                {"exitButton", "Выйти"},
                {"filtersLabel", "Фильтры"},
                {"idPrompt", "ID"},
                {"namePrompt", "Имя"},
                {"xPrompt", "X"},
                {"yPrompt", "Y"},
                {"creationDatePrompt", "Дата создания"},
                {"areaPrompt", "Площадь"},
                {"roomsPrompt", "Комнаты"},
                {"furnishPrompt", "Меблировка"},
                {"viewPrompt", "Вид"},
                {"transportPrompt", "Транспорт"},
                {"houseNamePrompt", "Название дома"},
                {"houseYearPrompt", "Год постройки дома"},
                {"houseLiftsPrompt", "Кол-во лифтов в доме"},
                {"ownerIdPrompt", "ID владельца"},
                {"actionsLabel", "Действия"},
                {"removeByIdLabel", "Удалить по ID:"},
                {"executeButton", "Выполнить"},
                {"removeAnyByViewLabel", "Удалить любой по виду:"},
                {"countGreaterThanFurnishLabel", "Посчитать больше чем меблировка:"},
                {"executeScriptLabel", "Выполнить скрипт:"},
                {"pathPrompt", "Путь к файлу скрипта"},
                {"chooseFileButton", "Выбрать файл"},
                {"infoButton", "Инфо"},
                {"clearButton", "Очистить"},
                {"saveButton", "Сохранить"},
                {"headButton", "Первый"},
                {"addButton", "Добавить"},
                {"addLabel", "Добавить"},
                {"updateButton", "Обновить"},
                {"showButton", "Показать"},
                {"minByIdButton", "Мин по ID"},
                {"filterButton", "Фильтр"},

                // Table Columns
                {"columnId", "ID"},
                {"columnName", "Имя"},
                {"columnX", "X"},
                {"columnY", "Y"},
                {"columnCreationDate", "Дата создания"},
                {"columnArea", "Площадь"},
                {"columnRooms", "Комнаты"},
                {"columnFurnish", "Меблировка"},
                {"columnView", "Вид"},
                {"columnTransport", "Транспорт"},
                {"columnHouseName", "Название дома"},
                {"columnHouseYear", "Год дома"},
                {"columnLifts", "Лифты дома"},
                {"columnUserOwnerId", "ID владельца"},
                {"columnActions", "Действия"},

                // Alert messages
                {"invalidId", "Неверный ID. Пожалуйста, введите число."},
                {"selectViewPrompt", "Пожалуйста, выберите вид для удаления."},
                {"selectFurnishPrompt", "Пожалуйста, выберите тип меблировки для подсчета."},
                {"scriptPathEmpty", "Путь к скрипту не может быть пустым."},
                {"errorLoadingFlatForm", "Ошибка загрузки формы квартиры: "},
                {"errorLoadingFlatUpdateForm", "Ошибка загрузки формы обновления квартиры: "},
                {"errorLoadingFlatInfoWindow", "Ошибка загрузки окна информации о квартире: "},
                {"server", "Сервер"},
                {"jsonParsingError", "Ошибка парсинга JSON для команды '"},
                {"serverErrorTitle", "Ошибка сервера"},
                {"serverResponseTitle", "Ответ сервера"},

                {"chooseScriptFileTitle", "Выберите файл скрипта"},
                {"textFiles", "Текстовые файлы"},
                {"allFiles", "Все файлы"},

                {"addFlatTitle", "Добавить квартиру"},
                {"updateFlatTitle", "Обновить квартиру"},
                {"flatInformationTitle", "Информация о квартире"},

                // Locale selection
                {"language", "Язык:"},
                {"ru_RU", "Русский"},
                {"fi_FI", "Финский"},
                {"it_IT", "Итальянский"},
                {"es_NI", "Испанский (Никарагуа)"},

                {"flatInfoTitle", "Информация о квартире"},
                {"coordinates:", "Координаты:"},
                {"house:", "Дом:"},
                {"editButton", "Редактировать"},
                {"closeButton", "Закрыть"},
                {"columnId:", "ID:"},
                {"columnName:", "Имя:"},
                {"columnCreationDate:", "Дата создания:"},
                {"columnArea:", "Площадь:"},
                {"columnRooms:", "Количество комнат:"},
                {"columnFurnish:", "Меблировка:"},
                {"columnView:", "Вид:"},
                {"columnTransport:", "Транспорт:"},
                {"columnUserOwnerId:", "ID владельца:"},
                {"columnHouse:", "Дом:"},

                {"enterFlatButton", "Ввести данные квартиры"},
                {"addIfMaxLabel", "Добавить если максимальный"},
                {"removeLowerLabel", "Удалить меньшие"},
                {"helpLabel", "Помощь"},
                {"clearLabel", "Очистить"},
                {"countByFurnishLabel", "Посчитать по меблировке"},
                {"historyLabel", "История"},
                {"infoLabel", "Информация"},

                {"formTitle", "Ввести квартиру"},
                {"formNameLabel", "Имя:"},
                {"formNamePrompt", "Введите имя квартиры"},
                {"formXLabel", "X (Координаты):"},
                {"formXPrompt", "Число (макс 497)"},
                {"formYLabel", "Y (Координаты):"},
                {"formYPrompt", "Число (> -738)"},
                {"formAreaLabel", "Площадь:"},
                {"formAreaPrompt", "Площадь (> 0)"},
                {"formRoomsLabel", "Количество комнат:"},
                {"formRoomsPrompt", "Кол-во комнат (1-20)"},
                {"formFurnishLabel", "Меблировка:"},
                {"formViewLabel", "Вид:"},
                {"formTransportLabel", "Транспорт:"},
                {"formHouseNameLabel", "Название дома:"},
                {"formHouseNamePrompt", "Название дома"},
                {"formHouseYearLabel", "Год постройки дома:"},
                {"formHouseYearPrompt", "Год постройки (> 0)"},
                {"formLiftsLabel", "Количество лифтов:"},
                {"formLiftsPrompt", "Кол-во лифтов (> 0)"},
                {"formSaveButton", "Сохранить"},
                {"formCancelButton", "Отмена"},

                {"authTitle", "Вход в систему"},
                {"authUsernamePrompt", "Имя пользователя"},
                {"authPasswordPrompt", "Пароль"},
                {"authLoginButton", "Войти"},
                {"authRegisterButton", "Регистрация"},

                {"authStageTitle", "Flat Collector - Авторизация"}
        };
    }
}