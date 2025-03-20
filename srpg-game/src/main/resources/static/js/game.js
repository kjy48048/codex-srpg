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

// 게임 상태
let selectedUnit = null;
let highlightedTiles = [];
let actionMode = null; // 'move', 'attack', null

// 게임 초기화
function initGame() {
    // 로딩 표시
    showLoading();
    
    // 새 게임 생성
    createNewGame();
    
    // 이벤트 리스너 등록
    endTurnButton.addEventListener('click', endTurn);
    moveButton.addEventListener('click', startMoveMode);
    attackButton.addEventListener('click', startAttackMode);
    cancelButton.addEventListener('click', cancelAction);
    newGameButton.addEventListener('click', createNewGame);
    homeButton.addEventListener('click', () => window.location.href = '/');
}

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
    
    fetch(`/api/game/${gameId}`)
    .then(response => response.json())
    .then(data => {
        gameState = data;
        renderGameBoard();
        updateTurnDisplay();
        updateActionButtons();
        hideLoading();
        
        // 게임 종료 조건 확인
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
    const isPlayerUnit = hasSelectedUnit && isPlayerUnit(selectedUnit);
    const isPlayerTurn = gameState && gameState.playerTurn;
    
    moveButton.disabled = !(hasSelectedUnit && isPlayerUnit && isPlayerTurn && !actionMode);
    attackButton.disabled = !(hasSelectedUnit && isPlayerUnit && isPlayerTurn && !actionMode);
    cancelButton.disabled = !actionMode;
}

// 타일 클릭 처리
function handleTileClick(x, y) {
    const tile = getTile(x, y);
    if (!tile) return;
    
    // 액션 모드에 따른 처리
    if (actionMode === 'move') {
        handleMoveAction(x, y);
    } else if (actionMode === 'attack') {
        handleAttackAction(x, y);
    } else {
        // 유닛 선택
        if (tile.unit) {
            selectedUnit = tile.unit;
            displayUnitInfo(tile.unit);
            
            // 선택 효과음 (나중에 추가)
            // playSound('select');
        } else {
            selectedUnit = null;
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
    
    // 로딩 표시
    showLoading();
    
    // 서버에 이동 요청
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
            // 이동 효과음 (나중에 추가)
            // playSound('move');
            
            // 게임 상태 갱신
            fetchGameState();
            
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
    
    // 공격 범위 내에 있고 적 유닛인지 확인
    if (isInAttackRange(x, y) && !isPlayerUnit(targetTile.unit)) {
        // 로딩 표시
        showLoading();
        
        // 서버에 공격 요청
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
                // 공격 효과음 (나중에 추가)
                // playSound('attack');
                
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
    
    // 서버에 턴 종료 요청
    fetch(`/api/game/${gameId}/end-turn`, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 게임 상태 초기화
            selectedUnit = null;
            actionMode = null;
            highlightedTiles = [];
            displayUnitInfo(null);
            
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

// 게임 시작
document.addEventListener('DOMContentLoaded', initGame);
