package com.itgroup;

import com.itgroup.bean.Member;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        MemberManager manager = new MemberManager();

        while(true){
            System.out.println("\n메뉴 선택");
            System.out.println("0:종료, 1:회원 목록 조회, 2:회원가입, 3:회원탈퇴, 4:총 회원수, 5:회원정보 수정, 6:회원정보, 7:성별로 검색, 8: xx");

            int menu = Integer.parseInt(scan.nextLine());
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

                    System.out.print("급여: ");
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

                    manager.deleteOne(id);
                    break;

                case 4:
                    manager.getSize();
                    break;

                case 5:
                    System.out.print("아이디: ");
                    id = scan.nextLine();

                    Member updateMember = new Member();

                    System.out.print("변경할 이름 (건너뛰려면 Enter): ");
                    String name = scan.nextLine();
                    if (!name.isBlank()) {
                        updateMember.setName(name);
                    }

                    System.out.print("변경할 비밀번호 (건너뛰려면 Enter): ");
                    String password = scan.nextLine();
                    if (!password.isBlank()) {
                        updateMember.setPassword(password);
                    }

                    System.out.print("변경할 성별 (건너뛰려면 Enter): ");
                    String gender = scan.nextLine();
                    if (!gender.isBlank()) {
                        updateMember.setGender(gender);
                    }

                    System.out.print("변경할 생년월일 (yyyy-MM-dd) (건너뛰려면 Enter): ");
                    String birth = scan.nextLine();
                    if (!birth.isBlank()) {
                        updateMember.setBirth(birth);
                    }

                    System.out.print("변경할 결혼 여부 (건너뛰려면 Enter): ");
                    String marriage = scan.nextLine();
                    if (!marriage.isBlank()) {
                        updateMember.setMarriage(marriage);
                    }

                    System.out.print("변경할 급여 (건너뛰려면 Enter): ");
                    String salaryInput = scan.nextLine();
                    if (!salaryInput.isBlank()) {
                        int salary = Integer.parseInt(salaryInput);
                        updateMember.setSalary(salary);
                    }

                    System.out.print("변경할 주소 (건너뛰려면 Enter): ");
                    String address = scan.nextLine();
                    if (!address.isBlank()) {
                        updateMember.setAddress(address);
                    }

                    System.out.print("변경할 관리자명 (건너뛰려면 Enter): ");
                    String managerName = scan.nextLine();
                    if (!managerName.isBlank()) {
                        updateMember.setManager(managerName);
                    }

                    manager.updateOne(id, updateMember);
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