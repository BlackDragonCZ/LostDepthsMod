package cz.blackdragoncz.lostdepths.client.gui.element;

public interface IProgressInfoHandler {

    double getProgress();

    default boolean isActive() {
        return true;
    }

    interface IBooleanProgressInfoHandler extends IProgressInfoHandler {

        boolean fillProgressBar();

        @Override
        default double getProgress() {
            return fillProgressBar() ? 1 : 0;
        }
    }

}
