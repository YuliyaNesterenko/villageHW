package village;

import java.io.IOException;
import java.util.Scanner;

/*Написать приложение про историю деревни с 1650 по 1750 год:
Должен быть экземпляр класса Village, в котором тем или иным способом хранятся данные жителях (до 50 штук)
Жители принадлежат к следующим классам
Peasant
Witch
Vampire
Werewolf
У каждого жителя деревни должно быть уникальное имя и фамилия.
История деревни состоит из записей о событиях.
История формируется циклом, перебирающим каждый день и генерирующим 1 событие.
Каждый день должно произойти одно из четырех событий:

Житель приезжает в деревню (добавляется в Village)
Житель уезжает из деревни (убирается из Village)
Житель идет в гости к другому жителю
Все спят. Ничего не происходит.

Встреча в гостях приводит к следующим последствиям
Любой житель (кроме Witch) встречается с Witch в пятницу 13-е или 31 октября.
Вызывается MurderException и житель исчезает из деревни.
Любой житель встречается с Vampire

Если житель Peasant или Witch 5% шанс, что житель становится Vampire
5% Вызывается MurderException и житель исчезает из деревни.
1% Вызывается DawnException и Vampire исчезает из деревни.

Любой житель встречается с Werewolf в полнолуние
Вызывается MurderException и житель исчезает из деревни.
В остальных случаях происходит просто чаепитие.*/

public class StartClass {

    public static Village village = new Village();
    public static HistoryInFile hif =new HistoryInFile();

    public static void main (String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        boolean bFlag =true;
        while (bFlag){

            System.out.println("\nВыберите одно из следующих действий (введите число):");
            System.out.println("0. Выйти из программы");
            System.out.println("1. Запустить новую историю деревни с выбранными настройками.\n" +
                               "    По умолчанию установлены:\n" +
                               "     - период от начала 1650 до конца 1750 года;\n" +
                               "     - первое полнолуние - 01.01.1650;\n" +
                               "     - максимальное количество жителей деревни - 50;\n" +
                               "     - события будут записыватся в файл и выводится на экран");
            System.out.println("2. Просмотреть ранее записанную историю (при наличии).");
            System.out.println("3. Поменять стартовые настройки.");
            System.out.println("4. Отобразить текущие настройки");

            switch (scan.nextInt()){
                case 4:
                    getSettings();
                    break;
                case 3:
                    setSettings();
                    break;
                case 2:
                    seeHistory();
                    break;
                case 1:
                    start();
                    break;
                default:
                    bFlag=false;
            }
        }
    }

    public static void getSettings(){
        System.out.println("\nДата начала истории деревни: " + Village.DateToString(village.getStartDate()));
        System.out.println("Дата окончания истории деревни: " + Village.DateToString(village.getFinalDate()));
        System.out.println("Дата первого полнолуния: " + Village.DateToString(village.getFirstMoonLightDay()));
        System.out.println("Максимальное количество жителей: " +village.getCITIZENS_QUANTITY());
        System.out.println("Возможность записи в файл: " + village.isWritingInFile());
        System.out.println("Возможность отображения на экране: " + village.isConsoleDisplay());
    }

    public static void setSettings(){
        Scanner scan = new Scanner(System.in);
        boolean bFlag =true;
        while (bFlag){

            System.out.println("\nЧто хотите изменить (введите число)?");
            System.out.println("0. Вернуться в предыдущее меню");
            System.out.println("1. Дату начала истории деревни");
            System.out.println("2. Дату окончания истории деревни");
            System.out.println("3. Дату первого полнолуния");
            System.out.println("4. Максимальное количество жителей");
            System.out.println("5. Возможность записи в файл");
            System.out.println("6. Возможность отображения на экране");

            switch (scan.nextInt()){
                case 1:
                    System.out.println("Введите дату в формате dd.mm.yyyy");
                    Scanner input1 = new Scanner(System.in);
                    village.setStartDate(Village.stringToDate(input1.nextLine()));
                    break;
                case 2:
                    System.out.println("Введите дату в формате dd.mm.yyyy");
                    Scanner input2 = new Scanner(System.in);
                    village.setFinalDate(Village.stringToDate(input2.nextLine()));
                    break;
                case 3:
                    System.out.println("Введите дату в формате dd.mm.yyyy");
                    Scanner input3 = new Scanner(System.in);
                    village.setFirstMoonLightDay(Village.stringToDate(input3.nextLine()));
                    break;
                case 4:
                    System.out.println("Введите натуральное число");
                    Scanner input4 = new Scanner(System.in);
                    village.setCITIZENS_QUANTITY(input4.nextInt());
                    break;
                case 5:
                    System.out.println("Записывать историю в файл (y/n)?");
                    Scanner input5 = new Scanner(System.in);
                    if (input5.nextLine().equals("y")){
                        village.setWritingInFile(true);
                    } else {
                        village.setWritingInFile(false);
                    }
                    break;
                case 6:
                    System.out.println("Выводить историю на экран (y/n)?");
                    Scanner input6 = new Scanner(System.in);
                    if (input6.nextLine().equals("y")){
                        village.setConsoleDisplay(true);
                    } else {
                        village.setConsoleDisplay(false);
                    }
                    break;
                default:
                    bFlag=false;
            }
        }
    }

    private static void seeHistory() throws IOException {
        // распечатать на экран файл
        Scanner scan = new Scanner(System.in);
        boolean bFlag =true;

        while (bFlag){
            System.out.println("Что выполнить (введите число)");
            System.out.println("0. Вернуться в предыдущее меню");
            System.out.println("1. Вывести на экран историю событий");
            System.out.println("2. Вывести на экран всех жителей на последнюю дату");
            System.out.println("3. Продолжить историю с последней даты");

            switch (scan.nextInt()){
                case 2:
                    HistoryInFile.citizensFromFile(true);
                    break;
                case 1:
                    HistoryInFile.historyFromFile(true);
                    break;
                case 3:
                    HistoryInFile.citizensFromFile(false);
                    HistoryInFile.historyFromFile(false);
                    village = HistoryInFile.getVillage();
                    System.out.println("Установите до какой даты продолжать историю в формате dd.mm.yyyy.\n" +
                            "Последняя дата в текщей истории: "+Village.DateToString(village.getStartDate()));
                    Scanner scanner3=new Scanner(System.in);
                    village.setFinalDate(Village.stringToDate(scanner3.nextLine()));

                default:
                    bFlag=false;
            }
        }
    }

    public static void start() throws IOException {
        boolean reWrite=true;
        if (village.getCitizens().size()==0) {
            village.generateCitizens();
            reWrite=false;
        }
        if (village.isWritingInFile()){
            hif.initHistoryWriter(reWrite);
        }
        village.lifeCycle(hif);
    }
}
