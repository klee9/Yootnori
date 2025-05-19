<table style="width:100%; text-align: left;">
  <tr>
    <td>UC ID and Name:</td>
    <td colspan="3">UC-02: 랜덤 윷 던지기</td>
  </tr>
  <tr>
    <td>Created By:</td>
    <td>Team 14</td>
    <td>Date Created:</td>
    <td>2025-04-08</td>
  </tr>
  <tr>
    <td>Primary Actor:</td>
    <td colspan="3">플레이어</td>
  </tr>
  <tr>
    <td>Level:</td>
    <td colspan="3">user goal</td>
  </tr>
  <tr>
    <td>Trigger:</td>
    <td colspan="3">플레이어가 랜덤 윷 던지기 버튼을 누름</td>
  </tr>
  <tr>
    <td>Stakeholders and Interests</td>
    <td colspan="3">
시스템:

- 윷 던지기 결과를 랜덤하게 게임 상태에 반영하고자 함 <br><br>

사용자 (자기 차례):
- 윷을 던져 말을 움직이고 싶음
- 가능하다면 상대의 말을 잡고자 함 <br><br>

사용자 (다른 차례):
- 상대가 공평하게 윷을 던졌는지 알고 싶어함
- 윷 던지기 결과에 따라 자신의 전략을 수정하고자 함
    </td>
  </tr>
  <tr>
    <td>Description:</td>
    <td colspan="3">플레이어가 윷을 던지기 위해 랜덤 윷 던지기 버튼을 클릭하는 상황</td>
  </tr>
  <tr>
    <td>Preconditions:</td>
    <td colspan="3">플레이어의 차례여야 함</td>
  </tr>
  <tr>
    <td>Postconditions:</td>
    <td colspan="3">
      결과에 따라 말 이동 또는 추가 행동(윷, 모)이 결정됨
    </td>
  </tr>
  <tr>
    <td>Main Success Scenario:</td>
    <td colspan="3">
        1. 플레이어가 '랜덤 윷 던지기' 버튼을 누른다 <br>
        2. 시스템이 무작위로 결과를 선택한다 <br>
        3. 결과가 스크린에 표시된다 <br>
    </td>
  </tr>
  <tr>
    <td>Extensions:</td>
    <td colspan="3">
      2a. 결과가 '윷' 혹은 '모'일 경우 <br>
        <ol style="margin-left: 20px;">
            <li>'윷', '모'가 나오지 않을 때까지 계속해서 버튼을 누른다</li>
            <li>결과를 저장한다</li>
        </ol>
     </td>
  </tr>
  <tr>
   <td>Priority:</td>
   <td colspan="3">높음</td>
  </tr>
  <tr>
   <td>Frequency of Use:</td>
   <td colspan="3">'랜덤 윷 던지기' 버튼을 누를 때마다</td>
  </tr>
  <tr>
   <td>Associated Information:</td>
   <td colspan="3">윷 정보</td>
  </tr>
  <tr>
   <td>Related Use Cases:</td>
   <td colspan="3">UC-04: 말 움직이기</td>
  </tr>
  <tr>
   <td>Open Issues:</td>
   <td colspan="3">없음</td>
  </tr>
</table>

Last Update: 2025-04-14