package com.github.thedeathlycow.thermoo.api.temperature.event;

/**
 * Ensures that changes in environmental effects, such as temperature from
 * {@link com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware} or wetness from a {@link com.github.thedeathlycow.thermoo.api.temperature.Soakable}
 * that come from common sources dictated by the {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController}
 * are not applied more than once.
 */
public abstract class InitialEnvironmentChangeResult<T> {

    /**
     * Whether the initial change has been applied by a listener
     */
    private boolean appliedInitialChange = false;

    /**
     * The initial temperature change that triggered the event
     */
    private final int initialChange;

    public InitialEnvironmentChangeResult(int initialChange) {
        this.initialChange = initialChange;
    }

    /**
     * Applies the initial change to an affectee.
     *
     * @param affectee The affectee of the change
     * @param amount   The amount of change to apply
     */
    protected abstract void applyChange(T affectee, int amount);

    /**
     * @return Returns the initial change value of the result
     */
    public int getInitialChange() {
        return initialChange;
    }

    /**
     *
     * @return Returns true if the initial change was applied
     */
    public boolean isInitialChangeApplied() {
        return appliedInitialChange;
    }

    /**
     * Applies the initial change to the environment aware entity, if it has not already been applied
     */
    public final void applyInitialChange(T affectee) {
        if (!this.appliedInitialChange) {
            this.applyChange(affectee, this.initialChange);
            this.appliedInitialChange = true;
        }
    }
}
