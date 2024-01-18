package com.artifex.sonui.editor;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkPage;
import com.artifex.solib.SOAffineTransform;
import com.artifex.solib.SOPage;
import com.artifex.solib.animation.SOAnimationColourEffectCommand;
import com.artifex.solib.animation.SOAnimationCommand;
import com.artifex.solib.animation.SOAnimationDisposeCommand;
import com.artifex.solib.animation.SOAnimationEasings;
import com.artifex.solib.animation.SOAnimationFadeCommand;
import com.artifex.solib.animation.SOAnimationMoveCommand;
import com.artifex.solib.animation.SOAnimationPlotCommand;
import com.artifex.solib.animation.SOAnimationRenderCommand;
import com.artifex.solib.animation.SOAnimationRotateCommand;
import com.artifex.solib.animation.SOAnimationScaleCommand;
import com.artifex.solib.animation.SOAnimationSetOpacityCommand;
import com.artifex.solib.animation.SOAnimationSetPositionCommand;
import com.artifex.solib.animation.SOAnimationSetTransformCommand;
import com.artifex.solib.animation.SOAnimationSetVisibilityCommand;
import com.artifex.solib.animation.SOAnimationWaitForEventCommand;
import com.artifex.solib.animation.SOAnimationWaitForLayerCommand;
import com.artifex.solib.animation.SOAnimationWaitForTimeCommand;
import com.artifex.sonui.editor.SlideShowConductorAnimations.BlindsFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.BoxFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.CheckerFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.CircleFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.ColorAdjustAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.DiamondFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.DissolveFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.FadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.PlusFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.RandomBarsFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.SplitFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.StripsFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.WedgeFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.WheelFadeAnimation;
import com.artifex.sonui.editor.SlideShowConductorAnimations.WipeFadeAnimation;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public final class SlideShowConductor {
    public String TAG = "SlideShowConductor";
    public SOAnimationCommand[] anim;
    public int index;
    public ArrayList<SlideShowConductorLayer> layers;
    public boolean paused;
    public boolean running;
    public final ArDkDoc soDoc;
    public final ArDkPage soPage;
    public Timer taskTimer;
    public final SlideShowConductorViewManager viewManager;

    public final class SlideShowConductorColourEffectTask extends SlideShowConductorTask {
        public ColorAdjustAnimation colorAnim = null;

        public SlideShowConductorColourEffectTask(SOAnimationColourEffectCommand sOAnimationColourEffectCommand, SlideShowConductorView slideShowConductorView) {
            super();
            int i = sOAnimationColourEffectCommand.effect;
            if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5) {
                this.colorAnim = new ColorAdjustAnimation(sOAnimationColourEffectCommand.turns, sOAnimationColourEffectCommand.bouncing, (int) (sOAnimationColourEffectCommand.duration * 1000.0f), i, slideShowConductorView);
            }
        }

        public void end(SlideShowConductorLayer slideShowConductorLayer) {
            ColorAdjustAnimation colorAdjustAnimation = this.colorAnim;
            if (colorAdjustAnimation == null || !colorAdjustAnimation.valueAnimator.isStarted() || !this.colorAnim.valueAnimator.isRunning()) {
                super.end(slideShowConductorLayer);
                return;
            }
            this.colorAnim.valueAnimator.end();
            this.colorAnim = null;
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            ColorAdjustAnimation colorAdjustAnimation = this.colorAnim;
            if (colorAdjustAnimation != null && !colorAdjustAnimation.valueAnimator.isStarted() && !this.colorAnim.valueAnimator.isRunning()) {
                this.colorAnim.valueAnimator.start();
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class SlideShowConductorFadeTask extends SlideShowConductorTask {
        public float endOpacity = 1.0f;
        public FadeAnimation fadeAnim = null;
        public int profile;
        public float startOpacity = BitmapDescriptorFactory.HUE_RED;
        public SlideShowConductorView view;

        public SlideShowConductorFadeTask(SOAnimationFadeCommand sOAnimationFadeCommand, SlideShowConductorView slideShowConductorView) {
            super();
            int i = 0;
            this.profile = 0;
            this.view = slideShowConductorView;
            switch (sOAnimationFadeCommand.effect) {
                case 1:
                    BlindsFadeAnimation blindsFadeAnimation = new BlindsFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = blindsFadeAnimation;
                    blindsFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    blindsFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 2:
                    CheckerFadeAnimation checkerFadeAnimation = new CheckerFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = checkerFadeAnimation;
                    checkerFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    checkerFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 3:
                    DissolveFadeAnimation dissolveFadeAnimation = new DissolveFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = dissolveFadeAnimation;
                    dissolveFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    break;
                case 4:
                    RandomBarsFadeAnimation randomBarsFadeAnimation = new RandomBarsFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = randomBarsFadeAnimation;
                    randomBarsFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    randomBarsFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 5:
                    CircleFadeAnimation circleFadeAnimation = new CircleFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = circleFadeAnimation;
                    circleFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    circleFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 6:
                    BoxFadeAnimation boxFadeAnimation = new BoxFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = boxFadeAnimation;
                    boxFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    boxFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 7:
                    DiamondFadeAnimation diamondFadeAnimation = new DiamondFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = diamondFadeAnimation;
                    diamondFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    diamondFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 8:
                    PlusFadeAnimation plusFadeAnimation = new PlusFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = plusFadeAnimation;
                    plusFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    plusFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 9:
                    SplitFadeAnimation splitFadeAnimation = new SplitFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = splitFadeAnimation;
                    splitFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    splitFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 10:
                    StripsFadeAnimation stripsFadeAnimation = new StripsFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = stripsFadeAnimation;
                    stripsFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    stripsFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 11:
                    WedgeFadeAnimation wedgeFadeAnimation = new WedgeFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = wedgeFadeAnimation;
                    wedgeFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    break;
                case 12:
                    WheelFadeAnimation wheelFadeAnimation = new WheelFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = wheelFadeAnimation;
                    wheelFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    wheelFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
                case 13:
                    WipeFadeAnimation wipeFadeAnimation = new WipeFadeAnimation(sOAnimationFadeCommand.turns, sOAnimationFadeCommand.bouncing, (int) (sOAnimationFadeCommand.duration * 1000.0f), slideShowConductorView);
                    this.fadeAnim = wipeFadeAnimation;
                    wipeFadeAnimation.transitionType = sOAnimationFadeCommand.endOpacity == BitmapDescriptorFactory.HUE_RED ? 1 : i;
                    wipeFadeAnimation.subType = sOAnimationFadeCommand.subType;
                    break;
            }
            FadeAnimation fadeAnimation = this.fadeAnim;
            if (fadeAnimation != null) {
                fadeAnimation.mListener = new FadeAnimation.FadeListener() {
                };
            }
        }

        public void end(SlideShowConductorLayer slideShowConductorLayer) {
            FadeAnimation fadeAnimation = this.fadeAnim;
            if (fadeAnimation == null || !fadeAnimation.hasStarted() || this.fadeAnim.hasEnded()) {
                super.end(slideShowConductorLayer);
                return;
            }
            FadeAnimation fadeAnimation2 = this.fadeAnim;
            fadeAnimation2.cancel();
            fadeAnimation2.viewToAnim.setOpacity(fadeAnimation2.transitionType == 0 ? 1.0f : BitmapDescriptorFactory.HUE_RED);
            this.fadeAnim = null;
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            FadeAnimation fadeAnimation = this.fadeAnim;
            if (fadeAnimation == null || fadeAnimation.hasStarted() || this.fadeAnim.hasEnded()) {
                int i = this.profile;
                float f = this.startOpacity;
                float ease = SOAnimationEasings.ease(i, f, this.endOpacity - f, this.progress);
                if (ease != slideShowConductorLayer.opacity) {
                    slideShowConductorLayer.opacity = ease;
                    slideShowConductorLayer.opacityChanged = true;
                }
            } else {
                this.view.startAnimation(this.fadeAnim);
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class SlideShowConductorLayer {
        public final ArrayList<SlideShowConductorTask> activeTasks = new ArrayList<>();
        public final int layerID;
        public float opacity = 1.0f;
        public boolean opacityChanged = true;
        public final PointF position = new PointF();
        public boolean positionChanged = true;
        public float rotation = BitmapDescriptorFactory.HUE_RED;
        public boolean rotationChanged = true;
        public boolean scaleChanged = true;
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        public SOAffineTransform transform = new SOAffineTransform();
        public boolean transformChanged = true;
        public SlideShowConductorView view = null;
        public boolean visibilityChanged = true;
        public boolean visible = false;
        public final ArrayList<WaitingTaskList> waitingTasks = new ArrayList<>();
        public int zPosition = 0;
        public boolean zPositionChanged = true;

        public SlideShowConductorLayer(int i) {
            this.layerID = i;
        }

        public void addTask(SlideShowConductorTask slideShowConductorTask) {
            if (isWaiting()) {
                ArrayList<WaitingTaskList> arrayList = this.waitingTasks;
                arrayList.get(arrayList.size() - 1).deferredTasks.add(slideShowConductorTask);
            } else {
                this.activeTasks.add(slideShowConductorTask);
            }
            SlideShowConductor.access$3500(SlideShowConductor.this);
        }

        public boolean isWaiting() {
            if (!this.waitingTasks.isEmpty()) {
                WaitingTaskList waitingTaskList = this.waitingTasks.get(0);
                if ((waitingTaskList.unpauseTime == null && waitingTaskList.unpauseOnCompleteLayer == null && waitingTaskList.unpauseOnStartLayer == null && waitingTaskList.unpauseEvent == -1) ? false : true) {
                    return true;
                }
            }
            return false;
        }

        public void purgeCompletedTasks() {
            ArrayList arrayList = new ArrayList();
            Iterator<SlideShowConductorTask> it = this.activeTasks.iterator();
            while (it.hasNext()) {
                SlideShowConductorTask next = it.next();
                if (next.completed) {
                    arrayList.add(next);
                }
            }
            this.activeTasks.removeAll(arrayList);
        }

        public void setPosition(PointF pointF) {
            if (!pointF.equals(this.position)) {
                PointF pointF2 = this.position;
                pointF2.x = pointF.x;
                pointF2.y = pointF.y;
                this.positionChanged = true;
            }
        }

        public final void unpause() {
            if (!isWaiting()) {
                Objects.requireNonNull(SlideShowConductor.this);
                if (!this.waitingTasks.isEmpty()) {
                    WaitingTaskList waitingTaskList = this.waitingTasks.get(0);
                    ArrayList<SlideShowConductorTask> arrayList = waitingTaskList.deferredTasks;
                    if (arrayList.size() > 0) {
                        float diffTime = SlideShowConductor.diffTime(waitingTaskList.pauseTime, new Date());
                        waitingTaskList.pauseTime = null;
                        Objects.requireNonNull(SlideShowConductor.this);
                        Iterator<SlideShowConductorTask> it = arrayList.iterator();
                        while (it.hasNext()) {
                            SlideShowConductorTask next = it.next();
                            next.startTime = new Date(next.startTime.getTime() + ((long) (1000.0f * diffTime)));
                        }
                        this.activeTasks.addAll(arrayList);
                    }
                    this.waitingTasks.remove(0);
                    if (!SlideShowConductor.this.idle()) {
                        SlideShowConductor.access$3500(SlideShowConductor.this);
                    }
                }
            }
        }
    }

    public final class SlideShowConductorMoveTask extends SlideShowConductorTask {
        public PointF endPosition = new PointF();
        public int profile = 0;
        public PointF startPosition = new PointF();

        public SlideShowConductorMoveTask() {
            super();
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            int i = this.profile;
            float f = this.startPosition.x;
            float ease = SOAnimationEasings.ease(i, f, this.endPosition.x - f, this.progress);
            int i2 = this.profile;
            float f2 = this.startPosition.y;
            slideShowConductorLayer.setPosition(new PointF(ease, SOAnimationEasings.ease(i2, f2, this.endPosition.y - f2, this.progress)));
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class SlideShowConductorPlotTask extends SlideShowConductorTask {
        public PointF position = new PointF();
        public int zPosition = 0;

        public SlideShowConductorPlotTask() {
            super();
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            if (this.progress >= 1.0f) {
                slideShowConductorLayer.setPosition(this.position);
                if (true != slideShowConductorLayer.visible) {
                    slideShowConductorLayer.visible = true;
                    slideShowConductorLayer.visibilityChanged = true;
                }
                int i = this.zPosition;
                if (i != slideShowConductorLayer.zPosition) {
                    slideShowConductorLayer.zPosition = i;
                    slideShowConductorLayer.zPositionChanged = true;
                }
                SlideShowConductor.this.viewManager.add(slideShowConductorLayer.view);
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class SlideShowConductorRotateTask extends SlideShowConductorTask {
        public float endAngle = 1.0f;
        public int profile = 0;
        public float startAngle = BitmapDescriptorFactory.HUE_RED;

        public SlideShowConductorRotateTask() {
            super();
            new PointF();
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            int i = this.profile;
            float f = this.startAngle;
            float ease = SOAnimationEasings.ease(i, f, this.endAngle - f, this.progress);
            if (ease != slideShowConductorLayer.rotation) {
                slideShowConductorLayer.rotation = ease;
                slideShowConductorLayer.rotationChanged = true;
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class SlideShowConductorScaleTask extends SlideShowConductorTask {
        public float endScaleX = 1.0f;
        public float endScaleY = 1.0f;
        public int profile;
        public float startScaleX = BitmapDescriptorFactory.HUE_RED;
        public float startScaleY = BitmapDescriptorFactory.HUE_RED;

        public SlideShowConductorScaleTask() {
            super();
            new PointF();
            this.profile = 0;
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            int i = this.profile;
            float f = this.startScaleX;
            float ease = SOAnimationEasings.ease(i, f, this.endScaleX - f, this.progress);
            int i2 = this.profile;
            float f2 = this.startScaleY;
            float ease2 = SOAnimationEasings.ease(i2, f2, this.endScaleY - f2, this.progress);
            if (!(ease == slideShowConductorLayer.scaleX && ease2 == slideShowConductorLayer.scaleY)) {
                slideShowConductorLayer.scaleX = ease;
                slideShowConductorLayer.scaleY = ease2;
                slideShowConductorLayer.scaleChanged = true;
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class SlideShowConductorSetOpacityTask extends SlideShowConductorTask {
        public float opacity = BitmapDescriptorFactory.HUE_RED;

        public SlideShowConductorSetOpacityTask() {
            super();
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            if (this.progress >= 1.0f) {
                float f = this.opacity;
                if (f != slideShowConductorLayer.opacity) {
                    slideShowConductorLayer.opacity = f;
                    slideShowConductorLayer.opacityChanged = true;
                }
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class SlideShowConductorSetPositionTask extends SlideShowConductorTask {
        public PointF origin = new PointF();

        public SlideShowConductorSetPositionTask() {
            super();
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            if (this.progress >= 1.0f) {
                slideShowConductorLayer.setPosition(this.origin);
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class SlideShowConductorSetTransformTask extends SlideShowConductorTask {
        public final SOAffineTransform transform = new SOAffineTransform();

        public SlideShowConductorSetTransformTask() {
            super();
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            if (this.progress >= 1.0f) {
                SOAffineTransform sOAffineTransform = this.transform;
                if (!sOAffineTransform.equals(slideShowConductorLayer.transform)) {
                    slideShowConductorLayer.transform = sOAffineTransform;
                    slideShowConductorLayer.transformChanged = true;
                }
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class SlideShowConductorSetVisibilityTask extends SlideShowConductorTask {
        public boolean visible = false;

        public SlideShowConductorSetVisibilityTask() {
            super();
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            boolean z;
            if (this.progress >= 1.0f && (z = this.visible) != slideShowConductorLayer.visible) {
                slideShowConductorLayer.visible = z;
                slideShowConductorLayer.visibilityChanged = true;
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public abstract class SlideShowConductorTask {
        public boolean bounce = false;
        public boolean completed = false;
        public float delay = BitmapDescriptorFactory.HUE_RED;
        public float duration = BitmapDescriptorFactory.HUE_RED;
        public float progress = BitmapDescriptorFactory.HUE_RED;
        public boolean reverse = false;
        public Date startTime = new Date();
        public int turns = 1;

        public SlideShowConductorTask() {
        }

        public void end(SlideShowConductorLayer slideShowConductorLayer) {
            Objects.requireNonNull(SlideShowConductor.this);
            if (this.bounce && this.turns % 2 == 0) {
                this.reverse = !this.reverse;
            }
            this.progress = this.reverse ? BitmapDescriptorFactory.HUE_RED : 1.0f;
            this.completed = true;
            execute(slideShowConductorLayer);
        }

        public abstract void execute(SlideShowConductorLayer slideShowConductorLayer);
    }

    public final class UnblockLayerOnCompleteTask extends SlideShowConductorTask {
        public SlideShowConductorLayer layerToUnblock;

        public UnblockLayerOnCompleteTask(SlideShowConductorLayer slideShowConductorLayer) {
            super();
            this.layerToUnblock = slideShowConductorLayer;
            Objects.requireNonNull(SlideShowConductor.this);
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            if (this.progress >= 1.0f) {
                SlideShowConductorLayer slideShowConductorLayer2 = this.layerToUnblock;
                if (!slideShowConductorLayer2.waitingTasks.isEmpty() && slideShowConductorLayer == slideShowConductorLayer2.waitingTasks.get(0).unpauseOnCompleteLayer) {
                    slideShowConductorLayer2.waitingTasks.get(0).unpauseOnCompleteLayer = null;
                    slideShowConductorLayer2.unpause();
                }
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class UnblockLayerOnStartTask extends SlideShowConductorTask {
        public SlideShowConductorLayer layerToUnblock;

        public UnblockLayerOnStartTask(SlideShowConductorLayer slideShowConductorLayer) {
            super();
            this.layerToUnblock = slideShowConductorLayer;
            Objects.requireNonNull(SlideShowConductor.this);
        }

        public void execute(SlideShowConductorLayer slideShowConductorLayer) {
            if (this.progress >= 1.0f) {
                SlideShowConductorLayer slideShowConductorLayer2 = this.layerToUnblock;
                if (!slideShowConductorLayer2.waitingTasks.isEmpty() && slideShowConductorLayer == slideShowConductorLayer2.waitingTasks.get(0).unpauseOnStartLayer) {
                    slideShowConductorLayer2.waitingTasks.get(0).unpauseOnStartLayer = null;
                    slideShowConductorLayer2.unpause();
                }
            }
            Objects.requireNonNull(SlideShowConductor.this);
        }
    }

    public final class WaitingTaskList {
        public final ArrayList<SlideShowConductorTask> deferredTasks = new ArrayList<>();
        public Date pauseTime = null;
        public int unpauseEvent = -1;
        public SlideShowConductorLayer unpauseOnCompleteLayer = null;
        public SlideShowConductorLayer unpauseOnStartLayer = null;
        public Date unpauseTime = null;

        public WaitingTaskList(SlideShowConductor slideShowConductor) {
        }
    }

    public SlideShowConductor(ArDkDoc arDkDoc, ArDkPage arDkPage, SlideShowConductorViewManager slideShowConductorViewManager) {
        this.soDoc = arDkDoc;
        this.soPage = arDkPage;
        this.running = false;
        this.paused = true;
        this.viewManager = slideShowConductorViewManager;
        this.anim = ((SOPage) arDkPage).getAnimations();
    }

    public static void access$3500(SlideShowConductor slideShowConductor) {
        if (slideShowConductor.taskTimer == null && !slideShowConductor.idle()) {
            slideShowConductor.viewManager.animationsRunning();
            slideShowConductor.taskTimer = new Timer();
            slideShowConductor.taskTimer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        /* JADX WARNING: Removed duplicated region for block: B:40:0x0097  */
                        /* JADX WARNING: Removed duplicated region for block: B:41:0x0099  */
                        /* Code decompiled incorrectly, please refer to instructions dump. */
                        public void run() {
                            /*
                                r14 = this;
                                com.artifex.sonui.editor.SlideShowConductor$1 r0 = com.artifex.sonui.editor.SlideShowConductor.AnonymousClass1.this
                                com.artifex.sonui.editor.SlideShowConductor r0 = com.artifex.sonui.editor.SlideShowConductor.this
                                java.util.Objects.requireNonNull(r0)
                                java.util.Date r1 = new java.util.Date
                                r1.<init>()
                                boolean r2 = r0.paused
                                r3 = 0
                                if (r2 == 0) goto L_0x0013
                                goto L_0x01ad
                            L_0x0013:
                                java.util.ArrayList<com.artifex.sonui.editor.SlideShowConductor$SlideShowConductorLayer> r2 = r0.layers
                                if (r2 == 0) goto L_0x01ad
                                boolean r2 = r2.isEmpty()
                                if (r2 == 0) goto L_0x001f
                                goto L_0x01ad
                            L_0x001f:
                                java.util.ArrayList<com.artifex.sonui.editor.SlideShowConductor$SlideShowConductorLayer> r2 = r0.layers
                                java.util.Iterator r2 = r2.iterator()
                            L_0x0025:
                                boolean r4 = r2.hasNext()
                                if (r4 == 0) goto L_0x01ad
                                java.lang.Object r4 = r2.next()
                                com.artifex.sonui.editor.SlideShowConductor$SlideShowConductorLayer r4 = (com.artifex.sonui.editor.SlideShowConductor.SlideShowConductorLayer) r4
                                com.artifex.sonui.editor.SlideShowConductorView r5 = r4.view
                                if (r5 != 0) goto L_0x0036
                                goto L_0x0025
                            L_0x0036:
                                java.util.ArrayList<com.artifex.sonui.editor.SlideShowConductor$SlideShowConductorTask> r5 = r4.activeTasks
                                java.util.Iterator r5 = r5.iterator()
                            L_0x003c:
                                boolean r6 = r5.hasNext()
                                r7 = 0
                                r8 = 1
                                if (r6 == 0) goto L_0x0050
                                java.lang.Object r6 = r5.next()
                                com.artifex.sonui.editor.SlideShowConductor$SlideShowConductorTask r6 = (com.artifex.sonui.editor.SlideShowConductor.SlideShowConductorTask) r6
                                boolean r6 = r6.completed
                                if (r6 != 0) goto L_0x003c
                                r5 = 1
                                goto L_0x0051
                            L_0x0050:
                                r5 = 0
                            L_0x0051:
                                if (r5 == 0) goto L_0x0054
                                goto L_0x005a
                            L_0x0054:
                                boolean r5 = r4.isWaiting()
                                if (r5 != 0) goto L_0x005c
                            L_0x005a:
                                r5 = 0
                                goto L_0x00a0
                            L_0x005c:
                                java.util.ArrayList<com.artifex.sonui.editor.SlideShowConductor$WaitingTaskList> r5 = r4.waitingTasks
                                boolean r5 = r5.isEmpty()
                                if (r5 == 0) goto L_0x0066
                                r5 = r3
                                goto L_0x006e
                            L_0x0066:
                                java.util.ArrayList<com.artifex.sonui.editor.SlideShowConductor$WaitingTaskList> r5 = r4.waitingTasks
                                java.lang.Object r5 = r5.get(r7)
                                com.artifex.sonui.editor.SlideShowConductor$WaitingTaskList r5 = (com.artifex.sonui.editor.SlideShowConductor.WaitingTaskList) r5
                            L_0x006e:
                                if (r5 != 0) goto L_0x0071
                                goto L_0x0094
                            L_0x0071:
                                java.util.Date r6 = r5.unpauseTime
                                if (r6 == 0) goto L_0x0094
                                java.util.Date r6 = new java.util.Date
                                r6.<init>()
                                java.util.Date r9 = r5.unpauseTime
                                float r6 = com.artifex.sonui.editor.SlideShowConductor.diffTime(r9, r6)
                                r9 = 0
                                int r10 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
                                if (r10 <= 0) goto L_0x0087
                                r6 = 1
                                goto L_0x008e
                            L_0x0087:
                                int r6 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
                                if (r6 >= 0) goto L_0x008d
                                r6 = -1
                                goto L_0x008e
                            L_0x008d:
                                r6 = 0
                            L_0x008e:
                                if (r6 != r8) goto L_0x0094
                                r5.unpauseTime = r3
                                r5 = 1
                                goto L_0x0095
                            L_0x0094:
                                r5 = 0
                            L_0x0095:
                                if (r5 != 0) goto L_0x0099
                                r5 = 1
                                goto L_0x00a0
                            L_0x0099:
                                r4.unpause()
                                boolean r5 = r4.isWaiting()
                            L_0x00a0:
                                if (r5 == 0) goto L_0x00a3
                                goto L_0x0025
                            L_0x00a3:
                                java.util.ArrayList<com.artifex.sonui.editor.SlideShowConductor$SlideShowConductorTask> r5 = r4.activeTasks
                                java.util.Iterator r5 = r5.iterator()
                            L_0x00a9:
                                boolean r6 = r5.hasNext()
                                if (r6 == 0) goto L_0x0105
                                java.lang.Object r6 = r5.next()
                                com.artifex.sonui.editor.SlideShowConductor$SlideShowConductorTask r6 = (com.artifex.sonui.editor.SlideShowConductor.SlideShowConductorTask) r6
                                boolean r9 = r6.completed
                                if (r9 == 0) goto L_0x00ba
                                goto L_0x00a9
                            L_0x00ba:
                                java.util.Date r9 = r6.startTime
                                float r9 = com.artifex.sonui.editor.SlideShowConductor.diffTime(r9, r1)
                                float r10 = r6.delay
                                int r11 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1))
                                if (r11 >= 0) goto L_0x00c7
                                goto L_0x00a9
                            L_0x00c7:
                                float r9 = r9 - r10
                                float r10 = r6.duration
                                float r9 = r9 / r10
                                boolean r10 = r6.reverse
                                r11 = 1065353216(0x3f800000, float:1.0)
                                int r12 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
                                if (r12 < 0) goto L_0x00fb
                                int r12 = r6.turns
                                if (r12 <= 0) goto L_0x00ec
                                float r13 = (float) r12
                                int r13 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1))
                                if (r13 < 0) goto L_0x00ec
                                boolean r9 = r6.bounce
                                if (r9 == 0) goto L_0x00e7
                                int r12 = r12 % 2
                                if (r12 != 0) goto L_0x00e7
                                r9 = r10 ^ 1
                                r10 = r9
                            L_0x00e7:
                                r6.completed = r8
                                r9 = 1065353216(0x3f800000, float:1.0)
                                goto L_0x00fb
                            L_0x00ec:
                                boolean r12 = r6.bounce
                                if (r12 == 0) goto L_0x00fa
                                r12 = 1073741824(0x40000000, float:2.0)
                                float r12 = r9 % r12
                                int r12 = (r12 > r11 ? 1 : (r12 == r11 ? 0 : -1))
                                if (r12 < 0) goto L_0x00fa
                                r10 = r10 ^ 1
                            L_0x00fa:
                                float r9 = r9 % r11
                            L_0x00fb:
                                if (r10 == 0) goto L_0x00ff
                                float r9 = r11 - r9
                            L_0x00ff:
                                r6.progress = r9
                                r6.execute(r4)
                                goto L_0x00a9
                            L_0x0105:
                                boolean r5 = r4.visibilityChanged
                                if (r5 != 0) goto L_0x0123
                                boolean r5 = r4.opacityChanged
                                if (r5 != 0) goto L_0x0123
                                boolean r5 = r4.positionChanged
                                if (r5 != 0) goto L_0x0123
                                boolean r5 = r4.zPositionChanged
                                if (r5 != 0) goto L_0x0123
                                boolean r5 = r4.scaleChanged
                                if (r5 != 0) goto L_0x0123
                                boolean r5 = r4.rotationChanged
                                if (r5 != 0) goto L_0x0123
                                boolean r5 = r4.transformChanged
                                if (r5 != 0) goto L_0x0123
                                goto L_0x0025
                            L_0x0123:
                                com.artifex.sonui.editor.SlideShowConductorView r5 = r4.view
                                if (r5 != 0) goto L_0x0129
                                goto L_0x0025
                            L_0x0129:
                                r5.begin()
                                boolean r5 = r4.visibilityChanged
                                if (r5 == 0) goto L_0x0139
                                com.artifex.sonui.editor.SlideShowConductorView r5 = r4.view
                                boolean r6 = r4.visible
                                r5.setVisibility(r6)
                                r4.visibilityChanged = r7
                            L_0x0139:
                                boolean r5 = r4.opacityChanged
                                if (r5 == 0) goto L_0x0146
                                com.artifex.sonui.editor.SlideShowConductorView r5 = r4.view
                                float r6 = r4.opacity
                                r5.setOpacity(r6)
                                r4.opacityChanged = r7
                            L_0x0146:
                                boolean r5 = r4.positionChanged
                                if (r5 == 0) goto L_0x0153
                                com.artifex.sonui.editor.SlideShowConductorView r5 = r4.view
                                android.graphics.PointF r6 = r4.position
                                r5.setPosition(r6)
                                r4.positionChanged = r7
                            L_0x0153:
                                boolean r5 = r4.zPositionChanged
                                if (r5 == 0) goto L_0x0160
                                com.artifex.sonui.editor.SlideShowConductorView r5 = r4.view
                                int r6 = r4.zPosition
                                r5.setZPosition(r6)
                                r4.zPositionChanged = r7
                            L_0x0160:
                                boolean r5 = r4.scaleChanged
                                if (r5 == 0) goto L_0x016f
                                com.artifex.sonui.editor.SlideShowConductorView r5 = r4.view
                                float r6 = r4.scaleX
                                float r8 = r4.scaleY
                                r5.setScale(r6, r8)
                                r4.scaleChanged = r7
                            L_0x016f:
                                boolean r5 = r4.rotationChanged
                                if (r5 == 0) goto L_0x017c
                                com.artifex.sonui.editor.SlideShowConductorView r5 = r4.view
                                float r6 = r4.rotation
                                r5.setRotation(r6)
                                r4.rotationChanged = r7
                            L_0x017c:
                                boolean r5 = r4.transformChanged
                                if (r5 == 0) goto L_0x01a6
                                com.artifex.sonui.editor.SlideShowConductorView r5 = r4.view
                                com.artifex.solib.SOAffineTransform r6 = r4.transform
                                java.util.Objects.requireNonNull(r6)
                                android.graphics.Matrix r8 = new android.graphics.Matrix
                                r8.<init>()
                                float r9 = r6.f2132a
                                float r10 = r6.d
                                r8.setScale(r9, r10)
                                float r9 = r6.b
                                float r10 = r6.c
                                r8.setSkew(r9, r10)
                                float r9 = r6.x
                                float r6 = r6.y
                                r8.setTranslate(r9, r6)
                                r5.setTransform(r8)
                                r4.transformChanged = r7
                            L_0x01a6:
                                com.artifex.sonui.editor.SlideShowConductorView r4 = r4.view
                                r4.commit()
                                goto L_0x0025
                            L_0x01ad:
                                java.util.ArrayList<com.artifex.sonui.editor.SlideShowConductor$SlideShowConductorLayer> r1 = r0.layers
                                java.util.Iterator r1 = r1.iterator()
                            L_0x01b3:
                                boolean r2 = r1.hasNext()
                                if (r2 == 0) goto L_0x01c3
                                java.lang.Object r2 = r1.next()
                                com.artifex.sonui.editor.SlideShowConductor$SlideShowConductorLayer r2 = (com.artifex.sonui.editor.SlideShowConductor.SlideShowConductorLayer) r2
                                r2.purgeCompletedTasks()
                                goto L_0x01b3
                            L_0x01c3:
                                boolean r1 = r0.idle()
                                if (r1 == 0) goto L_0x01e5
                                java.util.Timer r1 = r0.taskTimer
                                if (r1 != 0) goto L_0x01ce
                                goto L_0x01d3
                            L_0x01ce:
                                r1.cancel()
                                r0.taskTimer = r3
                            L_0x01d3:
                                int r1 = r0.index
                                com.artifex.solib.animation.SOAnimationCommand[] r2 = r0.anim
                                int r2 = r2.length
                                if (r1 != r2) goto L_0x01e0
                                com.artifex.sonui.editor.SlideShowConductorViewManager r0 = r0.viewManager
                                r0.animationsCompleted()
                                goto L_0x01e5
                            L_0x01e0:
                                com.artifex.sonui.editor.SlideShowConductorViewManager r0 = r0.viewManager
                                r0.animationsWaiting()
                            L_0x01e5:
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.SlideShowConductor.AnonymousClass1.AnonymousClass1.run():void");
                        }
                    });
                }
            }, 0, 33);
        }
    }

    public static float diffTime(Date date, Date date2) {
        return ((float) (date2.getTime() - date.getTime())) / 1000.0f;
    }

    public final void consumeAnimationCommands() {
        boolean z;
        ArrayList<SlideShowConductorTask> arrayList;
        do {
            int i = this.index;
            SOAnimationCommand[] sOAnimationCommandArr = this.anim;
            z = true;
            if (i < sOAnimationCommandArr.length) {
                this.index = i + 1;
                SOAnimationCommand sOAnimationCommand = sOAnimationCommandArr[i];
                SlideShowConductorLayer animLayer = getAnimLayer(sOAnimationCommand.layer);
                if (sOAnimationCommand instanceof SOAnimationRenderCommand) {
                    SOAnimationRenderCommand sOAnimationRenderCommand = (SOAnimationRenderCommand) sOAnimationCommand;
                    SlideShowConductorView slideShowConductorView = animLayer.view;
                    if (slideShowConductorView != null) {
                        slideShowConductorView.dispose();
                        this.viewManager.remove(animLayer.view);
                    }
                    PointF pointF = sOAnimationRenderCommand.origin;
                    SlideShowConductorView newLayer = this.viewManager.newLayer(this.soDoc, this.soPage, sOAnimationRenderCommand.renderable, new PointF(pointF.x, pointF.y), new RectF(sOAnimationRenderCommand.x, sOAnimationRenderCommand.y, sOAnimationRenderCommand.w, sOAnimationRenderCommand.h));
                    animLayer.view = newLayer;
                    newLayer.render();
                } else if (sOAnimationCommand instanceof SOAnimationDisposeCommand) {
                    SOAnimationDisposeCommand sOAnimationDisposeCommand = (SOAnimationDisposeCommand) sOAnimationCommand;
                    SlideShowConductorView slideShowConductorView2 = animLayer.view;
                    if (slideShowConductorView2 != null) {
                        slideShowConductorView2.dispose();
                        this.viewManager.remove(animLayer.view);
                    }
                } else if (sOAnimationCommand instanceof SOAnimationWaitForTimeCommand) {
                    Date date = new Date(new Date().getTime() + ((long) (((SOAnimationWaitForTimeCommand) sOAnimationCommand).delay * 1000.0f)));
                    WaitingTaskList waitingTaskList = new WaitingTaskList(SlideShowConductor.this);
                    if (waitingTaskList.pauseTime == null) {
                        waitingTaskList.pauseTime = new Date();
                        waitingTaskList.unpauseTime = date;
                    }
                    animLayer.waitingTasks.add(waitingTaskList);
                } else if (sOAnimationCommand instanceof SOAnimationWaitForLayerCommand) {
                    SOAnimationWaitForLayerCommand sOAnimationWaitForLayerCommand = (SOAnimationWaitForLayerCommand) sOAnimationCommand;
                    int i2 = sOAnimationWaitForLayerCommand.whence;
                    float f = BitmapDescriptorFactory.HUE_RED;
                    if (i2 != 1) {
                        SlideShowConductorLayer animLayer2 = getAnimLayer(sOAnimationWaitForLayerCommand.waitee);
                        UnblockLayerOnStartTask unblockLayerOnStartTask = new UnblockLayerOnStartTask(animLayer);
                        unblockLayerOnStartTask.delay = BitmapDescriptorFactory.HUE_RED;
                        unblockLayerOnStartTask.duration = 0.001f;
                        animLayer2.addTask(unblockLayerOnStartTask);
                        WaitingTaskList waitingTaskList2 = new WaitingTaskList(SlideShowConductor.this);
                        if (waitingTaskList2.pauseTime == null) {
                            waitingTaskList2.pauseTime = new Date();
                            waitingTaskList2.unpauseOnStartLayer = animLayer2;
                        }
                        animLayer.waitingTasks.add(waitingTaskList2);
                    } else {
                        SlideShowConductorLayer animLayer3 = getAnimLayer(sOAnimationWaitForLayerCommand.waitee);
                        UnblockLayerOnCompleteTask unblockLayerOnCompleteTask = new UnblockLayerOnCompleteTask(animLayer);
                        if (!animLayer3.waitingTasks.isEmpty()) {
                            ArrayList<WaitingTaskList> arrayList2 = animLayer3.waitingTasks;
                            arrayList = arrayList2.get(arrayList2.size() - 1).deferredTasks;
                        } else {
                            arrayList = animLayer3.activeTasks;
                        }
                        Iterator<SlideShowConductorTask> it = arrayList.iterator();
                        while (it.hasNext()) {
                            SlideShowConductorTask next = it.next();
                            float f2 = (next.duration * ((float) next.turns)) + next.delay;
                            if (f2 > f) {
                                f = f2;
                            }
                        }
                        unblockLayerOnCompleteTask.delay = f;
                        unblockLayerOnCompleteTask.duration = 0.001f;
                        animLayer3.addTask(unblockLayerOnCompleteTask);
                        WaitingTaskList waitingTaskList3 = new WaitingTaskList(SlideShowConductor.this);
                        if (waitingTaskList3.pauseTime == null) {
                            waitingTaskList3.pauseTime = new Date();
                            waitingTaskList3.unpauseOnCompleteLayer = animLayer3;
                        }
                        animLayer.waitingTasks.add(waitingTaskList3);
                    }
                } else if (sOAnimationCommand instanceof SOAnimationWaitForEventCommand) {
                    int i3 = ((SOAnimationWaitForEventCommand) sOAnimationCommand).event;
                    WaitingTaskList waitingTaskList4 = new WaitingTaskList(SlideShowConductor.this);
                    if (waitingTaskList4.pauseTime == null) {
                        waitingTaskList4.pauseTime = new Date();
                        waitingTaskList4.unpauseEvent = i3;
                    }
                    animLayer.waitingTasks.add(waitingTaskList4);
                    continue;
                } else if (sOAnimationCommand instanceof SOAnimationPlotCommand) {
                    SOAnimationPlotCommand sOAnimationPlotCommand = (SOAnimationPlotCommand) sOAnimationCommand;
                    SlideShowConductorPlotTask slideShowConductorPlotTask = new SlideShowConductorPlotTask();
                    slideShowConductorPlotTask.delay = sOAnimationPlotCommand.delay;
                    slideShowConductorPlotTask.duration = 0.001f;
                    slideShowConductorPlotTask.position = sOAnimationPlotCommand.position;
                    slideShowConductorPlotTask.zPosition = sOAnimationPlotCommand.zPosition;
                    animLayer.addTask(slideShowConductorPlotTask);
                } else if (sOAnimationCommand instanceof SOAnimationSetVisibilityCommand) {
                    SOAnimationSetVisibilityCommand sOAnimationSetVisibilityCommand = (SOAnimationSetVisibilityCommand) sOAnimationCommand;
                    SlideShowConductorSetVisibilityTask slideShowConductorSetVisibilityTask = new SlideShowConductorSetVisibilityTask();
                    slideShowConductorSetVisibilityTask.delay = sOAnimationSetVisibilityCommand.delay;
                    slideShowConductorSetVisibilityTask.duration = 0.001f;
                    slideShowConductorSetVisibilityTask.visible = sOAnimationSetVisibilityCommand.visible;
                    animLayer.addTask(slideShowConductorSetVisibilityTask);
                } else if (sOAnimationCommand instanceof SOAnimationSetPositionCommand) {
                    SOAnimationSetPositionCommand sOAnimationSetPositionCommand = (SOAnimationSetPositionCommand) sOAnimationCommand;
                    SlideShowConductorSetPositionTask slideShowConductorSetPositionTask = new SlideShowConductorSetPositionTask();
                    slideShowConductorSetPositionTask.delay = sOAnimationSetPositionCommand.delay;
                    slideShowConductorSetPositionTask.duration = 0.001f;
                    slideShowConductorSetPositionTask.origin = sOAnimationSetPositionCommand.newOrigin;
                    animLayer.addTask(slideShowConductorSetPositionTask);
                } else if (sOAnimationCommand instanceof SOAnimationSetOpacityCommand) {
                    SOAnimationSetOpacityCommand sOAnimationSetOpacityCommand = (SOAnimationSetOpacityCommand) sOAnimationCommand;
                    SlideShowConductorSetOpacityTask slideShowConductorSetOpacityTask = new SlideShowConductorSetOpacityTask();
                    slideShowConductorSetOpacityTask.delay = sOAnimationSetOpacityCommand.delay;
                    slideShowConductorSetOpacityTask.duration = 0.001f;
                    slideShowConductorSetOpacityTask.opacity = sOAnimationSetOpacityCommand.opacity;
                    animLayer.addTask(slideShowConductorSetOpacityTask);
                } else if (sOAnimationCommand instanceof SOAnimationSetTransformCommand) {
                    SOAnimationSetTransformCommand sOAnimationSetTransformCommand = (SOAnimationSetTransformCommand) sOAnimationCommand;
                    SlideShowConductorSetTransformTask slideShowConductorSetTransformTask = new SlideShowConductorSetTransformTask();
                    SOAffineTransform sOAffineTransform = slideShowConductorSetTransformTask.transform;
                    sOAffineTransform.f2132a = sOAnimationSetTransformCommand.trfmA;
                    sOAffineTransform.b = sOAnimationSetTransformCommand.trfmB;
                    sOAffineTransform.c = sOAnimationSetTransformCommand.trfmC;
                    sOAffineTransform.d = sOAnimationSetTransformCommand.trfmD;
                    sOAffineTransform.x = sOAnimationSetTransformCommand.trfmX;
                    sOAffineTransform.y = sOAnimationSetTransformCommand.trfmY;
                    animLayer.addTask(slideShowConductorSetTransformTask);
                } else if (sOAnimationCommand instanceof SOAnimationMoveCommand) {
                    SOAnimationMoveCommand sOAnimationMoveCommand = (SOAnimationMoveCommand) sOAnimationCommand;
                    SlideShowConductorMoveTask slideShowConductorMoveTask = new SlideShowConductorMoveTask();
                    slideShowConductorMoveTask.bounce = sOAnimationMoveCommand.bouncing;
                    slideShowConductorMoveTask.reverse = sOAnimationMoveCommand.reversed;
                    slideShowConductorMoveTask.turns = sOAnimationMoveCommand.turns;
                    slideShowConductorMoveTask.delay = sOAnimationMoveCommand.delay;
                    slideShowConductorMoveTask.duration = sOAnimationMoveCommand.duration;
                    slideShowConductorMoveTask.startPosition = sOAnimationMoveCommand.start;
                    slideShowConductorMoveTask.endPosition = sOAnimationMoveCommand.end;
                    slideShowConductorMoveTask.profile = sOAnimationMoveCommand.profile;
                    animLayer.addTask(slideShowConductorMoveTask);
                } else if (sOAnimationCommand instanceof SOAnimationFadeCommand) {
                    SOAnimationFadeCommand sOAnimationFadeCommand = (SOAnimationFadeCommand) sOAnimationCommand;
                    SlideShowConductorFadeTask slideShowConductorFadeTask = new SlideShowConductorFadeTask(sOAnimationFadeCommand, animLayer.view);
                    slideShowConductorFadeTask.bounce = sOAnimationFadeCommand.bouncing;
                    slideShowConductorFadeTask.reverse = sOAnimationFadeCommand.reversed;
                    slideShowConductorFadeTask.turns = sOAnimationFadeCommand.turns;
                    slideShowConductorFadeTask.delay = sOAnimationFadeCommand.delay;
                    slideShowConductorFadeTask.duration = sOAnimationFadeCommand.duration;
                    slideShowConductorFadeTask.startOpacity = sOAnimationFadeCommand.startOpacity;
                    slideShowConductorFadeTask.endOpacity = sOAnimationFadeCommand.endOpacity;
                    slideShowConductorFadeTask.profile = sOAnimationFadeCommand.profile;
                    animLayer.addTask(slideShowConductorFadeTask);
                } else if (sOAnimationCommand instanceof SOAnimationScaleCommand) {
                    SOAnimationScaleCommand sOAnimationScaleCommand = (SOAnimationScaleCommand) sOAnimationCommand;
                    SlideShowConductorScaleTask slideShowConductorScaleTask = new SlideShowConductorScaleTask();
                    slideShowConductorScaleTask.bounce = sOAnimationScaleCommand.bouncing;
                    slideShowConductorScaleTask.reverse = sOAnimationScaleCommand.reversed;
                    slideShowConductorScaleTask.turns = sOAnimationScaleCommand.turns;
                    slideShowConductorScaleTask.delay = sOAnimationScaleCommand.delay;
                    slideShowConductorScaleTask.duration = sOAnimationScaleCommand.duration;
                    slideShowConductorScaleTask.startScaleX = sOAnimationScaleCommand.startX;
                    slideShowConductorScaleTask.startScaleY = sOAnimationScaleCommand.startY;
                    slideShowConductorScaleTask.endScaleX = sOAnimationScaleCommand.endX;
                    slideShowConductorScaleTask.endScaleY = sOAnimationScaleCommand.endY;
                    slideShowConductorScaleTask.profile = sOAnimationScaleCommand.profile;
                    animLayer.addTask(slideShowConductorScaleTask);
                } else if (sOAnimationCommand instanceof SOAnimationRotateCommand) {
                    SOAnimationRotateCommand sOAnimationRotateCommand = (SOAnimationRotateCommand) sOAnimationCommand;
                    SlideShowConductorRotateTask slideShowConductorRotateTask = new SlideShowConductorRotateTask();
                    slideShowConductorRotateTask.bounce = sOAnimationRotateCommand.bouncing;
                    slideShowConductorRotateTask.reverse = sOAnimationRotateCommand.reversed;
                    slideShowConductorRotateTask.turns = sOAnimationRotateCommand.turns;
                    slideShowConductorRotateTask.delay = sOAnimationRotateCommand.delay;
                    slideShowConductorRotateTask.duration = sOAnimationRotateCommand.duration;
                    slideShowConductorRotateTask.startAngle = sOAnimationRotateCommand.startAngle;
                    slideShowConductorRotateTask.endAngle = sOAnimationRotateCommand.endAngle;
                    slideShowConductorRotateTask.profile = sOAnimationRotateCommand.profile;
                    animLayer.addTask(slideShowConductorRotateTask);
                } else if (sOAnimationCommand instanceof SOAnimationColourEffectCommand) {
                    SOAnimationColourEffectCommand sOAnimationColourEffectCommand = (SOAnimationColourEffectCommand) sOAnimationCommand;
                    SlideShowConductorColourEffectTask slideShowConductorColourEffectTask = new SlideShowConductorColourEffectTask(sOAnimationColourEffectCommand, animLayer.view);
                    slideShowConductorColourEffectTask.bounce = sOAnimationColourEffectCommand.bouncing;
                    slideShowConductorColourEffectTask.reverse = sOAnimationColourEffectCommand.reversed;
                    slideShowConductorColourEffectTask.turns = sOAnimationColourEffectCommand.turns;
                    slideShowConductorColourEffectTask.delay = sOAnimationColourEffectCommand.delay;
                    slideShowConductorColourEffectTask.duration = sOAnimationColourEffectCommand.duration;
                    animLayer.addTask(slideShowConductorColourEffectTask);
                }
                if (this.index == this.anim.length) {
                    continue;
                } else {
                    z = false;
                    continue;
                }
            }
        } while (!z);
    }

    public void endAllCurrentTasks() {
        Iterator<SlideShowConductorLayer> it = this.layers.iterator();
        while (it.hasNext()) {
            SlideShowConductorLayer next = it.next();
            Objects.requireNonNull(SlideShowConductor.this);
            Iterator<SlideShowConductorTask> it2 = next.activeTasks.iterator();
            while (it2.hasNext()) {
                SlideShowConductorTask next2 = it2.next();
                if (!next2.completed) {
                    next2.end(next);
                }
            }
            next.purgeCompletedTasks();
            Iterator<WaitingTaskList> it3 = next.waitingTasks.iterator();
            while (it3.hasNext()) {
                Iterator<SlideShowConductorTask> it4 = it3.next().deferredTasks.iterator();
                while (it4.hasNext()) {
                    it4.next().end(next);
                }
            }
            next.waitingTasks.clear();
        }
    }

    public final SlideShowConductorLayer getAnimLayer(int i) {
        SlideShowConductorLayer slideShowConductorLayer;
        Iterator<SlideShowConductorLayer> it = this.layers.iterator();
        while (true) {
            if (!it.hasNext()) {
                slideShowConductorLayer = null;
                break;
            }
            slideShowConductorLayer = it.next();
            if (slideShowConductorLayer.layerID == i) {
                break;
            }
        }
        if (slideShowConductorLayer != null) {
            return slideShowConductorLayer;
        }
        SlideShowConductorLayer slideShowConductorLayer2 = new SlideShowConductorLayer(i);
        this.layers.add(slideShowConductorLayer2);
        return slideShowConductorLayer2;
    }

    public boolean getPaused() {
        return this.paused;
    }

    public boolean getRunning() {
        return this.running;
    }

    public final boolean idle() {
        Iterator<SlideShowConductorLayer> it = this.layers.iterator();
        while (true) {
            boolean z = true;
            if (!it.hasNext()) {
                return true;
            }
            SlideShowConductorLayer next = it.next();
            if (next.activeTasks.size() > 0) {
                break;
            } else if (next.waitingTasks.size() > 0) {
                if (next.waitingTasks.isEmpty() || next.waitingTasks.get(0).unpauseEvent == -1) {
                    z = false;
                }
                if (!z) {
                    break;
                }
            }
        }
        return false;
    }

    public boolean pageHasAnimations() {
        SOAnimationCommand[] sOAnimationCommandArr = this.anim;
        return sOAnimationCommandArr != null && sOAnimationCommandArr.length > 0;
    }

    public void pause() {
        if (!this.paused) {
            this.paused = true;
        }
    }

    public void resume() {
        if (this.paused) {
            this.paused = false;
        }
    }

    public boolean sendEvent(int i) {
        boolean z;
        boolean z2;
        ArrayList<SlideShowConductorLayer> arrayList = this.layers;
        if (arrayList == null) {
            return false;
        }
        Iterator<SlideShowConductorLayer> it = arrayList.iterator();
        while (it.hasNext()) {
            SlideShowConductorLayer next = it.next();
            if (next.waitingTasks.isEmpty() || next.waitingTasks.get(0).unpauseEvent != i) {
                z = false;
            } else {
                next.waitingTasks.get(0).unpauseEvent = -1;
                z = true;
            }
            if (z) {
                Objects.requireNonNull(SlideShowConductor.this);
                SlideShowConductor.this.endAllCurrentTasks();
                next.unpause();
                Objects.requireNonNull(SlideShowConductor.this);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        SlideShowConductor.this.consumeAnimationCommands();
                    }
                });
                z2 = true;
                continue;
            } else {
                z2 = false;
                continue;
            }
            if (z2) {
                return true;
            }
        }
        return false;
    }

    public void start() {
        SOAnimationCommand[] sOAnimationCommandArr;
        if (!this.running && (sOAnimationCommandArr = this.anim) != null && sOAnimationCommandArr.length != 0) {
            this.layers = new ArrayList<>();
            this.running = true;
            this.paused = false;
            consumeAnimationCommands();
            this.viewManager.animationsStarted();
        }
    }

    public void stop() {
        if (this.running) {
            this.running = false;
            this.paused = true;
            Timer timer = this.taskTimer;
            if (timer != null) {
                timer.cancel();
                this.taskTimer = null;
            }
            this.layers.clear();
        }
    }
}
