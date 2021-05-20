package gec.ui.utils.builders;

import java.awt.*;

public class GridBagConstraintsBuilder {

    private GridBagConstraintsBuilder() {
    }

    public static class Builder {
        private final GridBagConstraints constraints = new GridBagConstraints();

        public Builder withGridHeight(int gridHeight) {
            constraints.gridheight = gridHeight;
            return this;
        }

        public Builder withGridWidth(int gridWidth) {
            constraints.gridwidth = gridWidth;
            return this;
        }

        public Builder withFill(int fill) {
            constraints.fill = fill;
            return this;
        }

        public Builder withDefaultPadding() {
            constraints.insets = new Insets(10, 10, 10, 10);
            return this;
        }

        public Builder withDefaultWidthPadding() {
            constraints.insets = new Insets(0, 10, 0, 10);
            return this;
        }

        public Builder withDefaultHeightPadding() {
            constraints.insets = new Insets(10, 0, 10, 0);
            return this;
        }

        public Builder withWeightX(double weightX) {
            constraints.weightx = weightX;
            return this;
        }

        public Builder withWeightY(double weightY) {
            constraints.weighty = weightY;
            return this;
        }

        public Builder withAnchor(int anchor) {
            constraints.anchor = anchor;
            return this;
        }

        public Builder withFillOutWeight() {
            constraints.weightx = 1;
            constraints.weighty = 1;
            return this;
        }

        public GridBagConstraints build() {
            return constraints;
        }
    }
}
