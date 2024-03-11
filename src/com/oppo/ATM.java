package com.oppo;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private Account loginAcc;


    public void start(){
        while (true) {
            System.out.println("欢迎您进入Atm系统");
            System.out.println("1、用户登录");
            System.out.println("2、用户开户");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                default:
                    System.out.println("没有该操作");
            }
        }
    }

    private void login(){
        System.out.println("==系统登录==");
        if (accounts.size() == 0){
            System.out.println("当前系统中无任何账户，请先开户再来登录");
            return;
        }

        while (true) {
            System.out.println("请您输入您的登陆卡号：");
            String cardId = sc.next();
            Account acc = getAccountByCardId(cardId);
            if (acc == null){
                System.out.println("您输入的卡号不存在，请再次确认");
            }else {
                while (true) {
                    System.out.println("请您输入登录密码：");
                    String passWord = sc.next();
                    if (acc.getPassWord().equals(passWord)){
                        loginAcc = acc;
                        System.out.println("恭喜您，" + acc.getUserName() + "成功登陆了系统，您的卡号是；" + acc.getCardId());
                        showUserCommand();
                        return;
                    }else {
                        System.out.println("您输入的密码不正确，请确认");
                    }
                }
            }
        }
    }

    private void showUserCommand(){
        while (true) {
            System.out.println(loginAcc.getUserName() + "您可以选择如下功能进行账户的处理");
            System.out.println("1、查询账户");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、密码修改");
            System.out.println("6、退出");
            System.out.println("7、注销当前账户");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    showLoginAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    updatePassWord();
                    return;
                case 6:
                    System.out.println(loginAcc.getUserName() + "您退出系统成功！");
                    return;
                case 7:
                    if (deleteAccount()){
                        return;
                    }
                    break;
                default:
                    System.out.println("您当前选择的操作不存在，请确认");
            }
        }
    }

    private void updatePassWord() {
        System.out.println("==账户密码修改操作==");
        while (true) {
            System.out.println("请您输入当前账户的密码：");
            String passWord = sc.next();

            if (loginAcc.getPassWord().equals(passWord)){
                while (true) {
                    System.out.println("请您输入新密码：");
                    String newPassWord = sc.next();

                    System.out.println("请您再次输入新密码");
                    String okPassWord = sc.next();

                    if (okPassWord.equals(newPassWord)){
                        loginAcc.setPassWord(okPassWord);
                        System.out.println("恭喜您。您的密码修改成功");
                        return;
                    }else {
                        System.out.println("您输入的两次密码不一致");
                    }
                }
            }else {
                System.out.println("您当前输入的密码不正确");
            }
        }
    }

    private boolean deleteAccount() {
        System.out.println("==进行销户操作==");
        System.out.println("请问您确认销户吗？ y/n");
        String command = sc.next();
        switch (command){
            case "y":
                if (loginAcc.getMoney() == 0){
                    accounts.remove(loginAcc);
                    System.out.println("您好，您的账户已经成功销户");
                    return true;
                }else {
                    System.out.println("对不起，您的账户中存在金额，不允许销户");
                    return false;
                }
            default:
                System.out.println("好的，您的账户保留！！");
                return false;
        }
    }

    private void transferMoney() {
        System.out.println("==用户转账");
        if (accounts.size() < 2){
            System.out.println("当前系统中只有你一个账户，无法为其他账户转账");
            return;
        }

        if (loginAcc.getMoney() == 0){
            System.out.println("您的账户暂无余额，无法转账");
            return;
        }

        while (true) {
            System.out.println("请您输入对方的卡号：");
            String carId = sc.next();

            Account acc = getAccountByCardId(carId);
            if (acc == null){
                System.out.println("您输入的卡号不存在");
            }else {
                String name = "*" + acc.getUserName().substring(1);
                System.out.println("请您输入【" + name + "】的姓氏：");
                String preName = sc.next();
                if (acc.getUserName().startsWith(preName)){
                    while (true) {
                        System.out.println("请您输入转账给对方的金额：");
                        double money = sc.nextDouble();
                        if (loginAcc.getMoney() >= money){
                            loginAcc.setMoney(loginAcc.getMoney() - money);
                            acc.setMoney(acc.getMoney() + money);
                            System.out.println("您转账成功了");
                            return;
                        }else {
                            System.out.println("您余额不足，无法给对方转账，最多可转：" + loginAcc.getMoney());
                        }
                    }
                }else {
                    System.out.println("对不起，您认证的姓氏有问题");
                }
            }
        }
    }

    private void drawMoney() {
        System.out.println("==取钱操作==");
        if (loginAcc.getMoney() < 100){
            System.out.println("您的账户余额不足100元，不允许取钱");
            return;
        }

        while (true) {
            System.out.println("请您输入取款金额：");
            double money = sc.nextDouble();

            if (loginAcc.getMoney() >= money){
                if (money > loginAcc.getLimit()){
                    System.out.println("您当前取款金额超过了每次限额，您每次最多可取：" + loginAcc.getLimit());
                }else {
                    loginAcc.setMoney(loginAcc.getMoney() - money);
                    System.out.println("您取款：" + money + "成功，取款后您剩余：" + loginAcc.getMoney());
                    break;
                }
            }else {
                System.out.println("余额不足，您的账户中的余额是：" + loginAcc.getMoney());
            }
        }
    }

    private void depositMoney() {
        System.out.println("==存钱操作==");
        System.out.println("请您输入存款金额：");
        double money = sc.nextDouble();

        loginAcc.setMoney(loginAcc.getMoney() + money);
        System.out.println("恭喜您，您存钱：" + money + "成功，存钱后余额是：" + loginAcc.getMoney());
    }

    private void showLoginAccount(){
        System.out.println("==当前您的账户信息如下：==");
        System.out.println("卡号：" + loginAcc.getCardId());
        System.out.println("户主：" + loginAcc.getUserName());
        System.out.println("性别：" + loginAcc.getSex());
        System.out.println("余额：" + loginAcc.getMoney());
        System.out.println("每次取现额度：" + loginAcc.getLimit());
    }

    private void createAccount(){
        System.out.println("==系统开户系统==");
        Account acc = new Account();

        System.out.println("请您输入您的账户名称：");
        String name = sc.next();
        acc.setUserName(name);

        while (true) {
            System.out.println("请输入您的性别：");
            char sex = sc.next().charAt(0);
            if (sex == '男' || sex == '女'){
                acc.setSex(sex);
                break;
            }else {
                System.out.println("您输入的性别有误");
            }
        }

        while (true) {
            System.out.println("请您输入您的账户密码：");
            String passWord = sc.next();
            System.out.println("请您输入您的确认密码：");
            String okPassWord = sc.next();
            if (okPassWord.equals(passWord)){
                acc.setPassWord(okPassWord);
                break;
            }else {
                System.out.println("您输入的2次密码不一致，请您确认");
            }
        }

        System.out.println("请您输入您的取现额度：");
        double limit = sc.nextDouble();
        acc.setLimit(limit);

        String newCardId = createCardId();
        acc.setCardId(newCardId);

        accounts.add(acc);
        System.out.println("恭喜您，" + acc.getUserName() + "开户成功，您的卡号是：" + acc.getCardId());
    }

    private String createCardId(){
        while (true) {
            String cardId = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                int data = r.nextInt(10);
                cardId += data;
            }

            Account acc = getAccountByCardId(cardId);
            if (acc == null){
                return cardId;
            }
        }
    }

    private Account getAccountByCardId(String cardId){
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getCardId().equals(cardId)){
                return acc;
            }
        }
        return null;
    }
}