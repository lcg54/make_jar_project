package com.itgroup;

import com.itgroup.bean.Member;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        MemberManager manager = new MemberManager();

        while(true){
            System.out.println("메뉴 선택");
            System.out.println("0:종료, 1:회원 목록 조회, 2:회원가입, 3:급여 갱신, 4:총 회원수, 5:회원탈퇴, 6:회원정보, 7:성별로 검색, 8:xx");

            int menu = scan.nextInt();
            switch (menu){
                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0); // 운영체제에 종료됨을 알리고 빠져 나가기
                    break;

                case 1:
                    manager.selectAll();
                    break;

                case 2:
                    Member member = new Member();

                    System.out.print("아이디: ");
                    member.setId(scan.nextLine());

                    System.out.print("이름: ");
                    member.setName(scan.nextLine());

                    System.out.print("비밀번호: ");
                    member.setPassword(scan.nextLine());

                    System.out.print("성별: ");
                    member.setGender(scan.nextLine());

                    System.out.print("생년월일 (yyyy-MM-dd): ");
                    member.setBirth(scan.nextLine());

                    System.out.print("결혼 여부: ");
                    member.setMarriage(scan.nextLine());

                    System.out.print("연봉: ");
                    member.setSalary(Integer.parseInt(scan.nextLine()));

                    System.out.print("주소: ");
                    member.setAddress(scan.nextLine());

                    System.out.print("담당자: ");
                    member.setManager(scan.nextLine());

                    manager.addOne(member);
                    break;

                case 3:
                    System.out.print("아이디: ");
                    String id = scan.nextLine();

                    System.out.print("변경될 급여: ");
                    int salary = scan.nextInt();

                    manager.updateSalary(id, salary);
                    break;

                case 4:
                    manager.getSize();
                    break;

                case 5:
                    System.out.print("아이디: ");
                    id = scan.nextLine();

                    manager.deleteOne(id);
                    break;

                case 6:
                    System.out.print("아이디: ");
                    id = scan.nextLine();

                    manager.findById(id);
                    break;

                case 7:
                    System.out.print("성별: ");
                    String gen = scan.nextLine();

                    manager.findByGender(gen);
                    break;

                case 8:
                    break;
            }
        }

    }
}