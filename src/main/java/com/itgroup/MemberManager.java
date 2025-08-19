package com.itgroup;

import com.itgroup.bean.Member;
import com.itgroup.dao.MemberDao;

import java.util.List;

// 메인 클래스 대신 실제 모든 업무를 총 잭임지는 매니저 클래스
public class MemberManager {
    private MemberDao dao = null; // 실제 데이터베이스와 연동하는 다오 클래스

    public MemberManager() {
        this.dao =new MemberDao();
    }

    public void selectAll() { // 모든 회원 조회
        List<Member> list = dao.selectAll();
        if (list.size() == 0){
            System.out.println("회원이 존재하지 않습니다.");
        }else{
            System.out.println(list); // toString 오버라이딩
        }
    }

    public void getSize(){ // 회원수 조회
        int cnt = dao.getSize();
        if (cnt == 0){
            System.out.println("회원이 존재하지 않습니다.");
        }else{
            System.out.println("회원은 총 " + cnt + "명입니다.");
        }
    }

    public void addOne(Member member) {
        if (dao.addOne(member) > 0) {
            System.out.println("새 회원 \'" + member.getId() + "\' 님이 등록되었습니다.");
        } else {
            System.out.println("회원 등록 실패.");
        }
    }

    public void deleteOne(String id) {
        Member member = dao.deleteOne(id);
        if (member == null){
            System.out.println("일치하는 회원이 존재하지 않습니다.");
        }else {
            System.out.println("탈퇴할 회원 정보 : " + member);
            System.out.println("탈퇴가 완료되었습니다.");

        }
    }

    public void findById(String id) {
        Member member = dao.findById(id);
        if (member == null){
            System.out.println("일치하는 회원이 존재하지 않습니다.");
        }else {
            System.out.println("회원 정보 : " + member);
        }
    }

    public void updateSalary(String id, int salary) {
        Member member = dao.updateSalary(id, salary);
        if (member == null){
            System.out.println("일치하는 회원이 존재하지 않습니다.");
        }else {
            System.out.println("회원 정보가 다음과 같이 수정되었습니다." + member);
        }
    }


    public void findByGender(String gen) {
        List<Member> list = dao.findByGender(gen);
        if (list.size() == 0){
            System.out.println("회원이 존재하지 않습니다.");
        }else{
            System.out.println(list);
        }
    }
}
