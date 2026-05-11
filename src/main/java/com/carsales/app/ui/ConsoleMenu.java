package com.carsales.app.ui;

import java.util.Scanner;

public class ConsoleMenu {
    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMainMenu();
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1" -> System.out.println("[TODO] 销售前台模块");
                    case "2" -> System.out.println("[TODO] 库存管理模块");
                    case "3" -> System.out.println("[TODO] 报表中心模块");
                    case "0" -> {
                        System.out.println("退出系统。");
                        return;
                    }
                    default -> System.out.println("无效输入，请重试。\n");
                }
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n==== 汽车销售管理系统 ====");
        System.out.println("1. 销售前台");
        System.out.println("2. 库存管理");
        System.out.println("3. 报表中心");
        System.out.println("0. 退出");
        System.out.print("请输入选项: ");
    }
}
