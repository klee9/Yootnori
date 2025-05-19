<table style="width:100%; text-align: left;">
  <tr>
    <td>UC ID and Name:</td>
    <td colspan="3">UC-03: 지정 윷 던지기</td>
  </tr>
  <tr>
    <td>Created By:</td>
    <td>Team 14</td>
    <td>Date Created:</td>
    <td>2025-04-08</td>
  </tr>
  <tr>
    <td>Primary Actor:</td>
    <td colspan="3">테스터</td>
  </tr>
  <tr>
    <td>Level:</td>
    <td colspan="3">user goal</td>
  </tr>
  <tr>
    <td>Trigger:</td>
    <td colspan="3">플레이어가 지정 윷 던지기 버튼을 누름</td>
  </tr>
  <tr>
    <td>Stakeholders and Interests</td>
    <td colspan="3">
시스템: 

- 게임이 유효한 상태에서만 시작되도록 해야 함 
- 모든 초기 데이터를 정확하게 설정해야 함 <br><br>

메인 사용자:
- 게임이 올바르게 준비된 상태(플레이어 참여 완료, 설정 확인 등)에서만 시작되기를 원함
- 정확한 게임 진행을 기대함 <br><br>

다른 사용자:
- 게임 시작 전에 말 수(또는 턴 수)를 자유롭게 설정할 수 있기를 원함
- 게임의 난이도나 진행 시간을 자신들의 기호에 맞게 조정하고자 함
    </td>
  </tr>
  <tr>
    <td>Description:</td>
    <td colspan="3">테스터가 디버깅을 위해 지정된 윷 던지기 결과를 선택한다</td>
  </tr>
  <tr>
    <td>Preconditions:</td>
    <td colspan="3">-</td>
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
        1. 테스터가 '랜덤 윷 던지기' 버튼을 누른다 <br>
        2. 시스템이 지정된 선택 결과를 반환한다 <br>
        3. 결과가 스크린에 표시된다 <br>
    </td>
  </tr>
  <tr>
    <td>Extensions:</td>
    <td colspan="3">
      1a. 결과가 '윷' 혹은 '모'일 경우 <br>
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
   <td colspan="3">'지정 윷 던지기' 버튼을 누를 때마다</td>
  </tr>
  <tr>
   <td>Associated Information:</td>
   <td colspan="3">윷, 보드 정보</td>
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