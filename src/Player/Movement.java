package Player;

import GameScene.Building;
import Tools.Raycast;
import Main.GameHandler;
import Main.Main;
import Main.Renderer;

import javax.swing.plaf.InsetsUIResource;
import java.awt.*;
import java.util.Objects;

public class Movement {

    public static long deltaTime;
    private static final long lastTime = System.nanoTime() / 1000000;

    private static final Raycast raycast = new Raycast();

    public static float moveTime = 0;
    public static float jumpTime = 0;
    public static float dashTime = 0;
    public static float timeInAir = 0;

    public static final float maxSpeed = 3f;
    public static final float gravity = 2f / 1000000f;
    public static final float maxFallingSpeed = 2f / 100f;

    public static final float coyoteTime = 4000f;
    public static final int jumpDistanceToRegisterJump = 40;
    public static final float wallRunPadding = 5f;

    public static final float maxJumpHeight = 450f;
    public static final float dashDistance = 350f;

    public static final float walkingSpeed = 2.25f / 1000f;
    public static final float wallRunSpeed = 2.5f / 1000f;
    public static final float jumpSpeed = 2.3f / 10000f;
    public static final float jumpHeight = 1.01f;
    public static final float dashSpeed = 40f / 10000f;

    private static final boolean  checkBuildings = true;
    private static final boolean checkPlayers = true;

    public static void Calculate() {

        Main.player.isGrounded = false;
        Main.player.canJump = false;

        Main.player.isMoving = Main.player.left || Main.player.right;

        if(Main.player.isMoving) {
            moveTime += 0.0017;
        } else {
            moveTime = 0;
        }
        if(Main.player.isJumping && Main.player.affectedByGravity) {
            jumpTime += jumpSpeed;
        } else {
            jumpTime = 0;
        }
        if(Main.player.isDashing) {
            dashTime += dashSpeed;
        } else {
            dashTime = 0;
        }

        if((Main.player.y + Main.player.height) >= GameHandler.mapHeight) Main.player.isGrounded = true;

        if(Main.player.affectedByGravity && timeInAir > coyoteTime / 3) {
            ApplyGravity(Main.player);
        }

        if(Main.player.canMove) {
            BasicMovement(Main.player);
        }

        if(Main.player.isJumping) {
            UpdateJump(Main.player, jumpTime);
        }

        if(Main.player.isGrounded && jumpTime > 0.01) {
            Main.player.isJumping = false;
        }

        if(Main.player.affectedByGravity) {
            IsPlayerMovingInObject(Main.player, "down");
        }

        if(Main.player.isDashing) {

            Main.player.isJumping = false;
            jumpTime = 0;

            if(Objects.equals(Main.player.direction, "left")) {
                IsPlayerMovingInObject(Main.player, "left");
                Main.player.x = Main.player.lastXPositionBeforeDash - DashGraph(dashTime);
                if((Main.player.x + Main.player.width / 2) < (Main.player.lastXPositionBeforeDash - dashDistance)) {
                    ResetDash(Main.player);
                }
            } else if(Objects.equals(Main.player.direction, "right")) {
                IsPlayerMovingInObject(Main.player, "right");
                Main.player.x = Main.player.lastXPositionBeforeDash + DashGraph(dashTime);
                if((Main.player.x + Main.player.width / 2) > (Main.player.lastXPositionBeforeDash + dashDistance)) {
                    ResetDash(Main.player);
                }
            }
        }

        KeepPlayerInBounds(Main.player);

        if(!Main.player.isDashing) {
            IsPlayerMovingInObject(Main.player, "down");
        }

        if(raycast.Hit((int) Main.player.x + Main.player.width / 2, (int) Main.player.y + Main.player.height, jumpDistanceToRegisterJump, new Rectangle(0, GameHandler.mapHeight, GameHandler.mapWidth, 100))) {
            Main.player.canJump = true;
        }

        if(Main.player.isGrounded) {
            Main.player.dashCount = 0;
            if(Main.player.jumpCount >= 2) {
                Main.player.jumpCount = 0;
            }
            timeInAir = 0;

        } else {
            timeInAir += 1;
        }

    }

    public static void BasicMovement(PlayerPrefab _player) {

        float speed = SpeedGraph(moveTime, maxSpeed);
        speed = speed * walkingSpeed;

        if(_player.left && !_player.right && _player.x > 0) {
            _player.x -= speed;
            _player.direction = "left";
            IsPlayerMovingInObject(_player, "left");
        }

        if(_player.right && !_player.left && _player.x + _player.width < GameHandler.mapWidth) {
            _player.x += speed;
            _player.direction = "right";
            IsPlayerMovingInObject(_player, "right");
        }

        if(Main.player.rectOfWallRunBuilding == null) return;

        if(Main.player.up && !Main.player.down && Main.player.y > Main.player.rectOfWallRunBuilding.y) {
            Main.player.y -= wallRunSpeed;
            IsPlayerMovingInObject(Main.player, "up");
        }

        if(Main.player.down && !Main.player.up && Main.player.y + Main.player.height < Main.player.rectOfWallRunBuilding.y + Main.player.rectOfWallRunBuilding.height) {
            Main.player.y += wallRunSpeed;
            IsPlayerMovingInObject(Main.player, "down");
        }
    }

    public static void ApplyGravity(PlayerPrefab _player) {
        if(_player.isGrounded || _player.isWallRunning) return;
        _player.velocityY += gravity;
        if((_player.y + _player.height) <= (_player.lastYPositionOnGround - maxJumpHeight)) {
            _player.velocityY += 12.5 * gravity;
        }
        if(_player.velocityY > maxFallingSpeed) _player.velocityY = maxFallingSpeed;
        ApplyVelocity(_player);
        IsPlayerMovingInObject(_player, "down");
    }

    public static void ApplyVelocity(PlayerPrefab _player) {
        _player.x += _player.velocityX;
        _player.y += _player.velocityY;
    }

    public static float SpeedGraph(float _x, float _max) {
        // f(x) = 0.05x^3 + 0.1x
        // f(x) = x^0.6
        // f(x) = 0.0125x^2
        // f(x) = 0.125x

        // f(x) = 43(x-6.25)^-2 - 1.1

        //float y = (float) (0.05 * (_x * _x * _x) + 0.1 * _x);

        //float y = (float) Math.pow((double) _x, 0.6);

        //float y = (float) 0.0125 * (_x * _x);

        //float y = (float) 0.125 * _x;

        _x = _x / 2;
        if(_x > 3) _x = 3;
        float y = (float) (43 * Math.pow(_x, -2) - 1.1);
        y = 2 * y;
        //float y = _x;

        if(y > _max) y = _max;
        return y;
    }

    public static float JumpGraph(float _x) {
        // f(x) = -0.3(x-3.159)^2 + 3
        return (float) (-0.3 * Math.pow(_x - 3.159, 2) + 3);
    }

    public static float DashGraph(float _x) {
        // f(x) = x^0.4
        // f(x) = 2(x-2)

        //return (float) Math.pow(_x, (double) 0.9);
        //return (float) 2 * (_x - 10);

        return 5 * _x;
    }

    public static void IsPlayerMovingInObject(PlayerPrefab _player, String _direction) {

        boolean isCollidingPlayer = false;
        if(checkPlayers) {
            for (int i = 0; i < GameHandler.playerList.size(); i++) {
                Rectangle p1 = new Rectangle((int) _player.x, (int) _player.y, _player.width, _player.height);
                Rectangle p2 = new Rectangle((int) GameHandler.playerList.get(i).x, (int) GameHandler.playerList.get(i).y, GameHandler.playerList.get(i).width, GameHandler.playerList.get(i).height);
                if (!isCollidingPlayer && p1.intersects(p2)) {
                    isCollidingPlayer = true;
                    MovePlayerOutOfObject(p2, _direction, _player, true);
                }
            }
        }

        boolean isCollidingBuilding = false;
        if(checkBuildings) {
            for (int i = 0; i < GameHandler.buildingList.size(); i++) {
                if (!isCollidingBuilding && new Rectangle((int) _player.x, (int) _player.y, _player.width, _player.height).intersects(GameHandler.buildingList.get(i).rect)) {
                    isCollidingBuilding = true;
                    MovePlayerOutOfObject(GameHandler.buildingList.get(i).rect, _direction, _player, true);
                }

                if(new Rectangle((int) _player.x, (int) _player.y + 1, _player.width, _player.height).intersects(GameHandler.buildingList.get(i).rect)) _player.isGrounded = true;

                Building b = GameHandler.buildingList.get(i);

                if(raycast.Hit((int) _player.x + _player.width / 2, (int) _player.y + _player.height, jumpDistanceToRegisterJump, GameHandler.buildingList.get(i).rect)) {
                    _player.canJump = true;
                }

                if(_player.shouldWallRun && new Rectangle((int) (_player.x - wallRunPadding), (int)  _player.y, _player.width, _player.height).intersects(b.rect)) {
                    _player.isWallRunning = true;
                    _player.rectOfWallRunBuilding = b.rect;
                    _player.affectedByGravity = false;

                    if(_player.isJumping) {
                        ResetJump(_player);
                    }

                    return;
                }

                if(_player.shouldWallRun && new Rectangle((int) (_player.x + wallRunPadding), (int)  _player.y, _player.width, _player.height).intersects(b.rect)) {
                    _player.isWallRunning = true;
                    _player.rectOfWallRunBuilding = b.rect;
                    _player.affectedByGravity = false;

                    if(_player.isJumping) {
                        ResetJump(_player);
                    }

                    return;
                }

                _player.isWallRunning = false;
                _player.rectOfWallRunBuilding = null;
                if(!_player.isDashing) {
                    _player.affectedByGravity = true;
                }
            }
        }
    }

    public static void Jump(PlayerPrefab _player) {
        if(_player.jumpCount == 2) return;

        if(_player.isGrounded || _player.canJump) {
            _player.isJumping = true;
            _player.jumpCount = 1;
            _player.lastYPositionOnGround = _player.y;
            jumpTime = 0;
        } else if(timeInAir < coyoteTime && _player.jumpCount < 1) {
                _player.isJumping = true;
                _player.jumpCount = 1;
                _player.lastYPositionOnGround = _player.y;
            } else if (timeInAir > coyoteTime && _player.jumpCount < 1) {
                _player.jumpCount = 2;
                _player.isJumping = true;
                _player.lastYPositionOnGround = _player.y;
            } else if(_player.jumpCount == 1) {
                _player.jumpCount = 2;
                _player.isJumping = true;
                _player.lastYPositionOnGround = _player.y;
                jumpTime = 0;
            }
        }

    public static void UpdateJump(PlayerPrefab _player, float _jumpTime) {
        float y = -JumpGraph(_jumpTime) * 80;
        _player.y = _player.lastYPositionOnGround + (y * jumpHeight);
        IsPlayerMovingInObject(_player, "up");
    }

    public static void Dash(PlayerPrefab _player) {
        if(_player.left && _player.right || !_player.left && !_player.right || _player.dashCount >= 1) return;

        _player.lastXPositionBeforeDash = _player.x;
        _player.lastYPositionOnGround = _player.y;
        _player.isDashing = true;
        _player.affectedByGravity = false;
        _player.canMove = false;
        _player.dashCount = 1;
    }

    public static void KeepPlayerInBounds(PlayerPrefab _player) {
        if(_player.x <= 0) {
            _player.x = 0;
        }
        if(_player.y <= 0) {
            _player.y = 0;
            _player.velocityY = 0;
            _player.isJumping = false;
        }
        if((_player.x + _player.width) > GameHandler.mapWidth) {
            _player.x = GameHandler.mapWidth - _player.width;
        }
        if((_player.y + _player.height) >= GameHandler.mapHeight) {
            _player.y = GameHandler.mapHeight - _player.height;
            _player.isGrounded = true;
        }
    }

    public static void MovePlayerOutOfObject(Rectangle rect, String _direction, PlayerPrefab _player, boolean _groundPlayer) {
        switch (_direction) {
            case "up" -> {
                _player.y = (float) (rect.y + rect.height);
                _player.isJumping = false;
            }
            case "down" -> {
                if(_player.isDashing) return;
                _player.y = (float) (rect.y - _player.height);
                if(_groundPlayer) _player.isGrounded = true;
            }
            case "left" -> {
                _player.x = (float) (rect.x + rect.width);
                ResetDash(_player);
            }
            case "right" -> {
                _player.x = (float) (rect.x - _player.width);
                ResetDash(_player);
            }
        }
    }

    public static void ResetDash(PlayerPrefab _player) {
        _player.isDashing = false;
        dashTime = 0;
        _player.dashCount = 0;
        _player.canMove = true;
        _player.lastXPositionBeforeDash = _player.x;
        _player.affectedByGravity = true;
    }

    public static void ResetJump(PlayerPrefab _player) {
        _player.isJumping = false;
        _player.lastYPositionOnGround = _player.y;
        jumpTime = 0;
    }
}
