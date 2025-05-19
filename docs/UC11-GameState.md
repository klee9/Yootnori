<table style="width:100%; text-align: left;">
  <tr>
    <td>UC ID and Name:</td>
    <td colspan="3">UC-11: 게임 상태 표시</td>
  </tr>
  <tr>
    <td>Created By:</td>
    <td>Team 14</td>
    <td>Date Created:</td>
    <td>2025-04-08</td>
  </tr>
  <tr>
    <td>Primary Actor:</td>
    <td colspan="3">시스템</td>
  </tr>
  <tr>
    <td>Level:</td>
    <td colspan="3">sub-function</td>
  </tr>
  <tr>
    <td>Trigger:</td>
    <td colspan="3">플레이어의 차례가 변함</td>
  </tr>
  <tr>
    <td>Stakeholders and Interests</td>
    <td colspan="3">
시스템: 

- 변경 사항을 정확히 반영하고자 함
- 다양한 옵션을 제공하고자 함

사용자:
- 누구 차례인지와 현재 점수를 보고 싶음
- 현재 상황을 토대로 전략을 수정하고자 함
    </td>
  </tr>
  <tr>
    <td>Description:</td>
    <td colspan="3">게임 상태 (차례, 남은 말 등)을 표시한다</td>
  </tr>
  <tr>
    <td>Preconditions:</td>
    <td colspan="3">
    게임이 진행 중이어야 함
  </td>
  </tr>
  <tr>
  <td>Postconditions:</td>
  <td colspan="3">
    화면에 상태가 표시됨
  </td>
  </tr>
  <tr>
  <td>Main Success Scenario:</td>
  <td colspan="3">
    1. 현재 플레이어가 바뀐다 <br>
    2. 시스템에 의해 현재 상태(차례, 위치, 남은 말 등)가 반영된다 <br>
    3. UI가 업데이트 된다
  </td>
  </tr>
  <tr>
  <td>Extensions:</td>
  <td colspan="3">
    3a. 표시되는 상태와 현재 상태가 다른 경우 <br>
      <ol style="margin-left: 20px;">
          <li>시스템이 점검을 수행한다</li>
          <li>올바른 결과를 표시한다</li>
      </ol>
   </td>
  </tr>
  <tr>
   <td>Priority:</td>
   <td colspan="3">중간</td>
  </tr>
  <tr>
   <td>Frequency of Use:</td>
   <td colspan="3">플레이어의 차례가 바뀔 때마다</td>
  </tr>
  <tr>
   <td>Associated Information:</td>
   <td colspan="3">말, 플레이어, 보드 정보</td>
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