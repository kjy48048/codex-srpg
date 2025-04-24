// 게임 상태 관리
let gameState = null;
let gameId = null;

// DOM 요소
const gameBoardElement = document.getElementById('game-board');
const turnDisplayElement = document.getElementById('turn-display');
const endTurnButton = document.getElementById('end-turn-btn');
const selectedUnitInfoElement = document.getElementById('selected-unit-info');
const moveButton = document.getElementById('move-btn');
const attackButton = document.getElementById('attack-btn');
const cancelButton = document.getElementById('cancel-btn');
const gameMessageElement = document.getElementById('game-message');
const resultModal = document.getElementById('result-modal');
const resultTitle = document.getElementById('result-title');
const resultMessage = document.getElementById('result-message');
const newGameButton = document.getElementById('new-game-btn');
const homeButton = document.getElementById('home-btn');
const loadingElement = document.getElementById('loading');
// 유닛별 행동 추적: { [unitId]: { moved: boolean, attacked: boolean } }
let unitActions = {};

// 게임 상태
let selectedUnit = null;
let selectedUnitId = null;   // 새로 추가
let highlightedTiles = [];
let actionMode = null; // 'move', 'attack', null


// 로딩 표시
function showLoading() {
    loadingElement.classList.add('show');
}

// 로딩 숨기기
function hideLoading() {
    loadingElement.classList.remove('show');
}

// 게임 메시지 표시
function showMessage(message, duration = 3000) {
    gameMessageElement.textContent = message;
    gameMessageElement.classList.add('show');

    setTimeout(() => {
        gameMessageElement.classList.remove('show');
    }, duration);
}

// 게임 결과 모달 표시
function showResultModal(isVictory) {
    resultTitle.textContent = isVictory ? '승리!' : '패배!';
    resultMessage.textContent = isVictory
        ? '축하합니다! 모든 적을 물리쳤습니다.'
        : '게임 오버! 적에게 패배했습니다.';

    resultModal.classList.add('show');
}

// 새 게임 생성
function createNewGame() {
    // 로딩 표시
    showLoading();

    // 모달 숨기기
    resultModal.classList.remove('show');

    fetch('/api/game/new', {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        gameId = data.gameId;
        fetchGameState();
        showMessage('새 게임이 시작되었습니다!');
    })
    .catch(error => {
        console.error('Error creating new game:', error);
        hideLoading();
        showMessage('게임 생성 중 오류가 발생했습니다.', 5000);
    });
}

// 게임 상태 가져오기
function fetchGameState() {
    if (!gameId) return;

    return fetch(`/api/game/${gameId}`)
        .then(response => response.json())
        .then(data => {
            gameState = data;

            // (1) selectedUnit 갱신(가장 먼저)
            if (selectedUnitId) {
                let found = gameState.playerUnits.find(u => u.id === selectedUnitId);
                if (!found) {
                    found = gameState.enemyUnits.find(u => u.id === selectedUnitId);
                }
                selectedUnit = found || null;
            }

            // (2) 이제 최신 selectedUnit 정보가 반영된 상태에서 렌더링
            renderGameBoard();
            updateTurnDisplay();
            updateActionButtons();
            hideLoading();

            checkGameEnd();
        })
        .catch(error => {
            console.error('Error fetching game state:', error);
            hideLoading();
            showMessage('게임 상태 로딩 중 오류가 발생했습니다.', 5000);
        });
}



// 게임 보드 렌더링
function renderGameBoard() {
    if (!gameState) return;

    gameBoardElement.innerHTML = '';
    gameBoardElement.style.gridTemplateColumns = `repeat(${gameState.width}, 1fr)`;

    for (let y = 0; y < gameState.height; y++) {
        for (let x = 0; x < gameState.width; x++) {
            const tile = getTile(x, y);
            const tileElement = document.createElement('div');
            tileElement.className = 'tile';
            tileElement.dataset.x = x;
            tileElement.dataset.y = y;

            // 체스보드 패턴
            if ((x + y) % 2 === 1) {
                tileElement.classList.add('dark');
            }

            // 하이라이트된 타일
            if (isHighlighted(x, y)) {
                tileElement.classList.add('highlighted');
            }

            // 공격 범위
            if (isInAttackRange(x, y)) {
                tileElement.classList.add('attack-range');
            }

            // 선택된 유닛의 타일
            if (selectedUnit && selectedUnit.x === x && selectedUnit.y === y) {
                tileElement.classList.add('selected');
            }

            // 유닛이 있는 경우
            if (tile && tile.unit) {
                const unitElement = document.createElement('div');
                unitElement.className = `unit ${tile.unit.type.toLowerCase()}`;

                if (!isPlayerUnit(tile.unit)) {
                    unitElement.classList.add('enemy');
                }

                unitElement.textContent = tile.unit.name.charAt(0);
                tileElement.appendChild(unitElement);
            }

            // 타일 클릭 이벤트
            tileElement.addEventListener('click', () => handleTileClick(x, y));

            gameBoardElement.appendChild(tileElement);
        }
    }
}

// 타일 가져오기
function getTile(x, y) {
    if (!gameState || !gameState.tiles) return null;
    return gameState.tiles[`${x},${y}`];
}

// 하이라이트된 타일인지 확인
function isHighlighted(x, y) {
    return highlightedTiles.some(tile => tile.x === x && tile.y === y);
}

// 공격 범위 내인지 확인
function isInAttackRange(x, y) {
    if (actionMode !== 'attack' || !selectedUnit) return false;

    const distance = Math.abs(x - selectedUnit.x) + Math.abs(y - selectedUnit.y);
    return distance <= selectedUnit.attackRange && distance > 0;
}

// 플레이어 유닛인지 확인
function isPlayerUnit(unit) {
    if (!gameState || !gameState.playerUnits) return false;
    return gameState.playerUnits.some(u => u.id === unit.id);
}

// 턴 표시 업데이트
function updateTurnDisplay() {
    if (!gameState) return;
    turnDisplayElement.textContent = gameState.playerTurn ? '플레이어 턴' : '적 턴';

    // 적 턴일 때 버튼 비활성화
    if (!gameState.playerTurn) {
        moveButton.disabled = true;
        attackButton.disabled = true;
        endTurnButton.disabled = true;
    } else {
        endTurnButton.disabled = false;
        updateActionButtons();
    }
}

// 유닛 정보 표시
function displayUnitInfo(unit) {
    if (!unit) {
        selectedUnitInfoElement.innerHTML = '<p>유닛을 선택해주세요</p>';
        return;
    }

    const hpPercentage = (unit.currentHp / unit.maxHp) * 100;

    selectedUnitInfoElement.innerHTML = `
        <p><strong>${unit.name}</strong> (${unit.type})</p>
        <p>HP: ${unit.currentHp}/${unit.maxHp}</p>
        <div class="hp-bar">
            <div class="hp-fill" style="width: ${hpPercentage}%"></div>
        </div>
        <p>공격력: ${unit.attackPower}</p>
        <p>이동 범위: ${unit.moveRange}</p>
        <p>공격 범위: ${unit.attackRange}</p>
    `;
}

// 액션 버튼 상태 업데이트
function updateActionButtons() {
    const hasSelectedUnit = selectedUnit !== null;
    const isPlayerUnitSelected = hasSelectedUnit && isPlayerUnit(selectedUnit);
    const isPlayerTurn = gameState && gameState.playerTurn;

    // "이동 버튼"은 '유닛이 선택된 상태' && '플레이어 유닛' && '플레이어 턴' && '아직 이동 안 했음' && '액션 모드가 아님'
    moveButton.disabled = !(
        hasSelectedUnit &&
        isPlayerUnitSelected &&
        isPlayerTurn &&
        !actionMode &&
        canMoveUnit(selectedUnit)
    );

    // "공격 버튼"도 같은 방식
    attackButton.disabled = !(
        hasSelectedUnit &&
        isPlayerUnitSelected &&
        isPlayerTurn &&
        !actionMode &&
        canAttackUnit(selectedUnit)
    );

    // 취소 버튼은 액션 모드일 때만 (기존 로직과 동일)
    cancelButton.disabled = !actionMode;
}

// 타일 클릭 처리
function handleTileClick(x, y) {
    const tile = getTile(x, y);
    if (!tile) return;

    if (actionMode === 'move') {
        handleMoveAction(x, y);
    } else if (actionMode === 'attack') {
        handleAttackAction(x, y);
    } else {
        // 유닛 선택
        if (tile.unit) {
            selectedUnit = tile.unit;
            selectedUnitId = tile.unit.id;  // <--- 유닛 ID 저장
            displayUnitInfo(tile.unit);
        } else {
            selectedUnit = null;
            selectedUnitId = null;         // <--- 선택 해제 시
            displayUnitInfo(null);
        }
    }

    updateActionButtons();
    renderGameBoard();
}


// 이동 모드 시작
function startMoveMode() {
    if (!selectedUnit || !gameState.playerTurn) return;

    actionMode = 'move';
    highlightedTiles = calculateMoveRange(selectedUnit);
    updateActionButtons();
    renderGameBoard();

    showMessage('이동할 위치를 선택하세요');
}

// 공격 모드 시작
function startAttackMode() {
    if (!selectedUnit || !gameState.playerTurn) return;

    actionMode = 'attack';
    updateActionButtons();
    renderGameBoard();

    showMessage('공격할 대상을 선택하세요');
}

// 액션 취소
function cancelAction() {
    actionMode = null;
    highlightedTiles = [];
    updateActionButtons();
    renderGameBoard();

    showMessage('행동이 취소되었습니다');
}

// 이동 액션 처리
function handleMoveAction(x, y) {
    if (!isHighlighted(x, y)) return;
    if (!canMoveUnit(selectedUnit)) {
        showMessage('이미 이동을 사용했습니다.', 3000);
        return;
    }

    // 로딩 표시
    showLoading();

    fetch(`/api/game/${gameId}/move`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `unitId=${selectedUnit.id}&targetX=${x}&targetY=${y}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 이동 성공 -> 해당 유닛 'moved = true' 기록
                if (!unitActions[selectedUnit.id]) {
                    unitActions[selectedUnit.id] = { moved: false, attacked: false };
                }
                unitActions[selectedUnit.id].moved = true;

                // 게임 상태 갱신
                // 이동 성공 -> 서버 상태 최신화
                fetchGameState().then(() => {
                    // 여기서 selectedUnit이 새 좌표로 갱신됐을 것임
                    // "실제로 공격이 가능한 상태"면 곧바로 공격 모드 시작
                    if (canAttackUnit(selectedUnit) && hasEnemyInRange(selectedUnit)) {
                        actionMode = 'attack';
                        updateActionButtons();
                        renderGameBoard();
                        showMessage(`${selectedUnit.name}이(가) 이동 후 공격을 준비합니다!`);
                    } else {
                        // 공격 불가능하면 그냥 이동 메세지만
                        showMessage(`${selectedUnit.name}이(가) 이동했습니다`);
                    }
                });

                // 액션 모드 초기화
                actionMode = null;
                highlightedTiles = [];
                showMessage(`${selectedUnit.name}이(가) 이동했습니다`);
            } else {
                console.error('Move failed:', data.message);
                hideLoading();
                showMessage('이동할 수 없습니다', 3000);
            }
        })
        .catch(error => {
            console.error('Error moving unit:', error);
            hideLoading();
            showMessage('이동 중 오류가 발생했습니다', 3000);
        });
}


// 공격 액션 처리
function handleAttackAction(x, y) {
    const targetTile = getTile(x, y);
    if (!targetTile || !targetTile.unit) return;

    if (!canAttackUnit(selectedUnit)) {
        showMessage('이미 공격을 사용했습니다.', 3000);
        return;
    }

    // 공격 범위 내에 있고 적 유닛인지 확인
    if (isInAttackRange(x, y) && !isPlayerUnit(targetTile.unit)) {
        // 로딩 표시
        showLoading();

        fetch(`/api/game/${gameId}/attack`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `attackerUnitId=${selectedUnit.id}&targetUnitId=${targetTile.unit.id}`
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // 공격 성공 -> 해당 유닛 'attacked = true' 기록
                    if (!unitActions[selectedUnit.id]) {
                        unitActions[selectedUnit.id] = { moved: false, attacked: false };
                    }
                    unitActions[selectedUnit.id].attacked = true;

                    // 게임 상태 갱신
                    fetchGameState();
                    // 액션 모드 초기화
                    actionMode = null;
                    highlightedTiles = [];

                    showMessage(`${selectedUnit.name}이(가) 공격했습니다!`);
                } else {
                    console.error('Attack failed:', data.message);
                    hideLoading();
                    showMessage('공격할 수 없습니다', 3000);
                }
            })
            .catch(error => {
                console.error('Error attacking unit:', error);
                hideLoading();
                showMessage('공격 중 오류가 발생했습니다', 3000);
            });
    }
}


// 턴 종료
function endTurn() {
    // 로딩 표시
    showLoading();

    fetch(`/api/game/${gameId}/end-turn`, { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 현재 선택 및 액션 모드 초기화
                selectedUnit = null;
                actionMode = null;
                highlightedTiles = [];
                displayUnitInfo(null);

                // 이번 턴이 끝났으므로 유닛별 행동 기록을 초기화
                unitActions = {};

                showMessage('턴이 종료되었습니다. 적의 턴입니다...');

                // 게임 상태 갱신
                setTimeout(() => {
                    fetchGameState();
                }, 1000);
            } else {
                console.error('End turn failed:', data.message);
                hideLoading();
                showMessage('턴을 종료할 수 없습니다', 3000);
            }
        })
        .catch(error => {
            console.error('Error ending turn:', error);
            hideLoading();
            showMessage('턴 종료 중 오류가 발생했습니다', 3000);
        });
}


// 게임 종료 조건 확인
function checkGameEnd() {
    if (!gameState) return;

    if (gameState.playerUnits.length === 0) {
        // 패배
        showResultModal(false);
    } else if (gameState.enemyUnits.length === 0) {
        // 승리
        showResultModal(true);
    }
}

// 이동 범위 계산 (클라이언트 측 임시 구현)
function calculateMoveRange(unit) {
    const tilesInRange = [];
    const visited = {};
    const queue = [{x: unit.x, y: unit.y, distance: 0}];

    visited[`${unit.x},${unit.y}`] = true;

    while (queue.length > 0) {
        const current = queue.shift();

        if (current.distance <= unit.moveRange) {
            // 시작 위치가 아니면 이동 범위에 추가
            if (current.x !== unit.x || current.y !== unit.y) {
                tilesInRange.push({x: current.x, y: current.y});
            }

            // 상하좌우 타일 확인
            const directions = [
                {x: current.x - 1, y: current.y},
                {x: current.x + 1, y: current.y},
                {x: current.x, y: current.y - 1},
                {x: current.x, y: current.y + 1}
            ];

            for (const dir of directions) {
                if (dir.x >= 0 && dir.x < gameState.width && dir.y >= 0 && dir.y < gameState.height) {
                    const key = `${dir.x},${dir.y}`;
                    const tile = getTile(dir.x, dir.y);

                    if (!visited[key] && tile && tile.walkable && !tile.unit) {
                        visited[key] = true;
                        queue.push({x: dir.x, y: dir.y, distance: current.distance + tile.movementCost});
                    }
                }
            }
        }
    }

    return tilesInRange;
}

// 유닛 이동/공격 가능 여부를 확인하는 함수
function canMoveUnit(unit) {
    if (!unitActions[unit.id]) {
        // 아직 이 유닛 기록이 없으면 처음
        unitActions[unit.id] = { moved: false, attacked: false };
    }
    return !unitActions[unit.id].moved;
}

function canAttackUnit(unit) {
    if (!unitActions[unit.id]) {
        unitActions[unit.id] = { moved: false, attacked: false };
    }
    return !unitActions[unit.id].attacked;
}

// 적이 공격 범위 내에 하나라도 있는지 확인하는 함수
function hasEnemyInRange(unit) {
    if (!gameState || !gameState.enemyUnits || !unit) return false;

    return gameState.enemyUnits.some(enemyUnit => {
        // (이 게임에서 X/Y가 적 유닛에도 저장되어 있다고 가정)
        const distance = Math.abs(enemyUnit.x - unit.x) + Math.abs(enemyUnit.y - unit.y);
        return distance <= unit.attackRange && distance > 0;
    });
}

// 초기화 함수: 버튼 이벤트 핸들러 등록 및 초기 상태 설정
function initGame() {
    // 이동, 공격, 취소 모드 버튼
    moveButton.addEventListener('click', startMoveMode);
    attackButton.addEventListener('click', startAttackMode);
    cancelButton.addEventListener('click', cancelAction);
    // 턴 종료 버튼
    endTurnButton.addEventListener('click', endTurn);
    // 새 게임, 홈 버튼
    newGameButton.addEventListener('click', createNewGame);
    homeButton.addEventListener('click', () => {
        hideLoading();
        window.location.href = '/';
    });
}
// 게임 시작
document.addEventListener('DOMContentLoaded', initGame);

// 네임스페이스에 기능 모듈화
window.GameApp = {
    api: {
        createNewGame: createNewGame,
        fetchGameState: fetchGameState,
        move: handleMoveAction,
        attack: handleAttackAction,
        endTurn: endTurn
    },
    util: {
        calculateMoveRange: calculateMoveRange,
        isInAttackRange: isInAttackRange,
        hasEnemyInRange: hasEnemyInRange,
        canMoveUnit: canMoveUnit,
        canAttackUnit: canAttackUnit
    },
    ui: {
        showLoading: showLoading,
        hideLoading: hideLoading,
        showMessage: showMessage,
        showResultModal: showResultModal,
        displayUnitInfo: displayUnitInfo,
        updateTurnDisplay: updateTurnDisplay,
        updateActionButtons: updateActionButtons,
        renderGameBoard: renderGameBoard
    },
    actions: {
        handleTileClick: handleTileClick,
        startMoveMode: startMoveMode,
        startAttackMode: startAttackMode,
        cancelAction: cancelAction
    },
    init: initGame
};
