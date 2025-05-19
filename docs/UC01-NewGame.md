<table style="width:100%; text-align: left;">
  <tr>
    <td>UC ID and Name:</td>
    <td colspan="3">UC-01: 새로운 게임 시작</td>
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
    <td colspan="3">플레이어가 게임을 시작</td>
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
    <td colspan="3">플레이어가 게임을 시작하기 위해 필요한 설정을 완료하고 게임을 초기화하는 과정</td>
  </tr>
  <tr>
    <td>Preconditions:</td>
    <td colspan="3">없음</td>
  </tr>
  <tr>
    <td>Postconditions:</td>
    <td colspan="3">
      - 윷놀이 판과 게임 상태가 초기화됨<br>
      - UI에 초기 상태가 표시됨<br>
    </td>
  </tr>
  <tr>
    <td>Main Success Scenario:</td>
    <td colspan="3">
        1. 플레이어가 프로그램을 실행한다 <br>
        2. 플레이어 수를 설정한다 <br>
        3. 각 플레이어의 말 수를 설정한다 <br>
        4. 윷놀이 판과 말을 초기화한다 <br>
        5. 게임 시작 준비가 완료된다 <br>
    </td>
  </tr>
  <tr>
    <td>Extensions:</td>
    <td colspan="3">
      3a. 플레이어 수가 비정상적으로 입력된 경우<br>
        <ol style="margin-left: 20px;">
            <li>시스템은 오류 메시지를 출력하고 다시 입력을 요구한다.</li>
        </ol><br>
      4a. 말 수가 0 이하로 설정된 경우<br>
        <ol style="margin-left: 20px;">
            <li>기본값으로 자동 설정된다.</li>
        </ol>
    </td>
  </tr>
  <tr>
    <td>Priority:</td>
    <td colspan="3">높음</td>
  </tr>
  <tr>
    <td>Frequency of Use:</td>
    <td colspan="3">게임 시작 시마다</td>
  </tr>
  <tr>
    <td>Associated Information:</td>
    <td colspan="3">플레이어 수, 말 수, 게임 설정 정보</td>
  </tr>
  <tr>
    <td>Related Use Cases:</td>
    <td colspan="3">UC-02: 랜덤 윷 던지기, UC-03: 지정 윷 던지기</td>
  </tr>
  <tr>
    <td>Open Issues:</td>
    <td colspan="3">없음</td>
  </tr>
</table>

Last Update: 2025-04-14