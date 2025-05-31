<table style="width:100%; text-align: left;">
  <tr>
    <td>UC ID and Name:</td>
    <td colspan="3">UC-05: 말 업기</td>
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
    <td colspan="3">플레이어의 말이 동일한 위치에 있음</td>
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
    <td colspan="3">플레이어가 자신의 말을 업고자 한다</td>
  </tr>
  <tr>
    <td>Preconditions:</td>
    <td colspan="3">
    - 윷 던지기 결과가 있어야 함<br>
    - 업을 수 있는 말이 있어야 함
  </td>
  </tr>
  <tr>
  <td>Postconditions:</td>
  <td colspan="3">
    - 업은 말의 개수가 정확하게 기록됨 <br>
    - 업은 말이 함께 움직임 
  </td>
  </tr>
  <tr>
  <td>Main Success Scenario:</td>
  <td colspan="3">
      1. 플레이어가 윷 던지기 결과에 따라 말을 움직인다 <br>
      2. 이동한 칸에 자신의 말이 이미 존재한다면, 규칙에 따라 말을 업는다  <br>
      3. 선택된 말이 업히고, 업은 말은 하나로 간주되어 함께 움직인다 <br>
      4. 업힌 결과가 화면에 표시된다
  </td>
  </tr>
  <tr>
  <td>Extensions:</td>
  <td colspan="3">
    -
   </td>
  </tr>
  <tr>
   <td>Priority:</td>
   <td colspan="3">중간</td>
  </tr>
  <tr>
   <td>Frequency of Use:</td>
   <td colspan="3">플레이어의 말이 있는 칸으로 자신의 말이 움직일 때마다</td>
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