<table style="width:100%; text-align: left;">
  <tr>
    <td>UC ID and Name:</td>
    <td colspan="3">UC-06: 말 잡기</td>
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
    <td colspan="3">서로 다른 플레이어의 말이 동일한 위치에 있음</td>
  </tr>
  <tr>
    <td>Stakeholders and Interests</td>
    <td colspan="3">
시스템: 

- 말들의 상태를 정확히 기록하길 원함
- 규칙 위반을 방지하길 원함 <br><br>

사용자:
- 업힌 말이 상대에 의해 잡히는 것을 방지하고자 함
- 최대한 합리적인 결정을 내리고자 함
- 업고 최대한 멀리 가길 원함
    </td>
  </tr>
  <tr>
    <td>Description:</td>
    <td colspan="3">플레이어가 다른 플레이어의 말을 잡고자 한다</td>
  </tr>
  <tr>
    <td>Preconditions:</td>
    <td colspan="3">
    상대방의 말이 이동하고자 하는 위치에 있어야 함
  </td>
  </tr>
  <tr>
  <td>Postconditions:</td>
  <td colspan="3">
    - 상대방의 말이 시작점으로 되돌아감 <br>
    - 상대의 말을 잡은 플레이어가 추가 턴을 얻음 
  </td>
  </tr>
  <tr>
  <td>Main Success Scenario:</td>
  <td colspan="3">
    1. 플레이어가 윷 던지기 결과에 따라 말을 움직인다 <br>
    2. 이동한 칸에 상대의 말이 존재한다면, 규칙에 따라 말을 잡는다  <br>
    3. 게임 상태를 갱신하고 화면에 결과를 반영한다
  </td>
  </tr>
  <tr>
  <td>Extensions:</td>
  <td colspan="3">
    2a. 상대방의 말이 업혀 있는 경우 <br>
      <ol style="margin-left: 20px;">
          <li>업혀 있는 말을 모두 시작점으로 보낸다</li>
      </ol>
   </td>
  </tr>
  <tr>
   <td>Priority:</td>
   <td colspan="3">높음</td>
  </tr>
  <tr>
   <td>Frequency of Use:</td>
   <td colspan="3">상대 플레이어의 말이 있는 칸으로 말을 움직일 때마다</td>
  </tr>
  <tr>
   <td>Associated Information:</td>
   <td colspan="3">윷놀이 규칙, 보드에 위치한 말 정보</td>
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