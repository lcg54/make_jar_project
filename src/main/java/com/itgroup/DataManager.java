package com.itgroup;

import com.itgroup.bean.Board;
import com.itgroup.bean.Member;
import com.itgroup.dao.BoardDao;
import com.itgroup.dao.MemberDao;

import java.util.List;

// 메인 클래스 대신 실제 모든 업무를 총 잭임지는 매니저 클래스
public class DataManager {
    private MemberDao mdao = null; // 실제 데이터베이스와 연동하는 다오 클래스
    private BoardDao bdao = null;

    public DataManager() {
        this.mdao = new MemberDao();
        this.bdao = new BoardDao();
    }

    public void selectAll() { // 모든 회원 조회
        List<Member> list = mdao.selectAll();
        if (list.size() == 0){
            System.out.println("회원이 존재하지 않습니다.");
        }else{
            System.out.println(list); // toString 오버라이딩
        }
    }

    public void getSize(){ // 회원수 조회
        int cnt = mdao.getSize();
        if (cnt == 0){
            System.out.println("회원이 존재하지 않습니다.");
        }else{
            System.out.println("회원은 총 " + cnt + "명입니다.");
        }
    }

    public void addOne(Member member) {
        if (mdao.addOne(member) > 0) {
            System.out.println("새 회원 \'" + member.getId() + "\' 님이 등록되었습니다.");
        } else {
            System.out.println("회원 등록에 실패하였습니다.");
        }
    }

    public void deleteOne(String id) {
        Member member = mdao.deleteOne(id);
        if (member == null){
            System.out.println("일치하는 회원이 존재하지 않습니다.");
        }else {
            System.out.println("탈퇴할 회원 정보 : " + member);
            System.out.println("탈퇴가 완료되었습니다.");

        }
    }

    public void findById(String id) {
        Member member = mdao.findById(id);
        if (member == null){
            System.out.println("일치하는 회원이 존재하지 않습니다.");
        }else {
            System.out.println("회원 정보 : " + member);
        }
    }

    public void findByGender(String gen) {
        List<Member> list = mdao.findByGender(gen);
        if (list.size() == 0){
            System.out.println("일치하는 회원이 존재하지 않습니다.");
        }else{
            System.out.println(list);
        }
    }

    public void updateOne(String id, Member updateMember) {
        Member updated = mdao.updateOne(id, updateMember);
        if (updated == null) {
            System.out.println("일치하는 회원이 존재하지 않습니다.");
        } else {
            System.out.println("수정 완료: " + updated);
        }
    }

    // 여기부터 Board

    public void selectAllBoard() {
        List<Board> boardList = bdao.selectAll();

        for (Board board:boardList){
            int no = board.getNo();
            String writer = board.getWriter();
            String password = board.getPassword();
            String subject = board.getSubject();
            String content = board.getContent();
            int readhit = board.getReadhit();
            String regdate = board.getRegdate();
            System.out.println(no + "\t" + writer + "\t" + password + "\t" + subject + "\t" + content + "\t" + readhit + "\t" + regdate);
        }
    }

    public void selectEvenData() {
        List<Board> boardList = bdao.selectEven();
        System.out.println(boardList); // 위와 같으나 for문 대신 toString Overriding
    }

    public void selectOneData(String writer) {
        Board board = bdao.selectOne(writer);
        System.out.println(board);
    }
}
