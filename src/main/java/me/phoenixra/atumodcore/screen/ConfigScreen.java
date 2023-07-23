package me.phoenixra.atumodcore.screen;

import me.phoenixra.atumodcore.config.Config;
import me.phoenixra.atumodcore.config.ConfigEntry;
import me.phoenixra.atumodcore.gui.elements.GuiElementButton;
import me.phoenixra.atumodcore.gui.elements.GuiElementTextField;
import me.phoenixra.atumodcore.gui.menus.scroll.ScrollArea;
import me.phoenixra.atumodcore.gui.menus.scroll.ScrollAreaEntry;
import me.phoenixra.atumodcore.input.CharacterFilter;
import me.phoenixra.atumodcore.input.MouseInput;
import me.phoenixra.atumodcore.utils.NumberUtils;
import me.phoenixra.atumodcore.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigScreen extends GuiScreen {
    protected Config config;
    protected ScrollArea configList;
    protected GuiScreen parent;
    protected String title;
    protected GuiElementButton doneBtn;
    protected String activeDescription = null;

    protected Map<String, String> descriptions = new HashMap<String, String>();

    protected static final Color ENTRY_BACKGROUND_COLOR = new Color(92, 92, 92);
    protected static final Color SCREEN_BACKGROUND_COLOR = new Color(54, 54, 54);
    protected static final Color HEADER_FOOTER_COLOR = new Color(33, 33, 33);

    public ConfigScreen(Config config, String title, GuiScreen parent) {
        super();

        this.config = config;
        this.parent = parent;
        this.title = title;

        this.configList = new ScrollArea(0, 50, 300, 0);
        this.configList.backgroundColor = ENTRY_BACKGROUND_COLOR;

        for (String s : this.config.getCategorys()) {

            this.configList.addEntry(new CategoryConfigScrollAreaEntry(this.configList, s));

            for (ConfigEntry e : this.config.getEntrysForCategory(s)) {

                switch (e.getType()) {
                    case STRING:
                        this.configList.addEntry(new StringConfigScrollAreaEntry(this.configList, e));
                        break;
                    case INTEGER:
                        this.configList.addEntry(new IntegerConfigScrollAreaEntry(this.configList, e));
                        break;
                    case DOUBLE:
                        this.configList.addEntry(new DoubleConfigScrollAreaEntry(this.configList, e));
                        break;
                    case FLOAT:
                        this.configList.addEntry(new FloatConfigScrollAreaEntry(this.configList, e));
                        break;
                    case LONG:
                        this.configList.addEntry(new LongConfigScrollAreaEntry(this.configList, e));
                        break;
                    case BOOLEAN:
                        this.configList.addEntry(new BooleanConfigScrollAreaEntry(this.configList, e));
                        break;
                }

            }

        }

        this.doneBtn = new GuiElementButton(0, 0, 100, 20, "Done", true, (press) -> {
            Minecraft.getMinecraft().displayGuiScreen(this.parent);
        });
        colorizeButton(this.doneBtn);
        this.doneBtn.ignoreBlockedInput = true;
        this.doneBtn.ignoreLeftMouseDownClickBlock = true;

    }

    @Override
    public void initGui() {

        this.configList.x = (this.width / 2) - 150;
        this.configList.height = this.height - 100;

    }

    @Override
    public void onGuiClosed() {
        this.saveConfig();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        FontRenderer font = Minecraft.getMinecraft().fontRenderer;

        GlStateManager.enableBlend();

        //Draw screen background
        drawRect(0, 0, this.width, this.height, SCREEN_BACKGROUND_COLOR.getRGB());

        this.configList.render();

        //Draw header
        drawRect(0, 0, this.width, 50, HEADER_FOOTER_COLOR.getRGB());

        //Draw title
        if (this.title != null) {
            drawString(font, this.title, (this.width / 2) - (font.getStringWidth(this.title) / 2), 20, Color.WHITE.getRGB());
        }

        //Draw footer
        drawRect(0, this.height - 50, this.width, this.height, HEADER_FOOTER_COLOR.getRGB());

        this.doneBtn.x = (this.width / 2) - (this.doneBtn.width / 2);
        this.doneBtn.y = this.height - 35;
        this.doneBtn.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);

        for (ScrollAreaEntry e : this.configList.getEntries()) {
            if (!(e instanceof ConfigScrollAreaEntry) || !e.isHovered()) continue;
            String name = ((ConfigScrollAreaEntry) e).configEntry.getName();
            if (!this.descriptions.containsKey(name)) continue;

            if (!ConfigScrollAreaEntry.isHeaderFooterHovered()) {
                renderDescription(this.descriptions.get(name), mouseX, mouseY);
            }
            break;
        }

    }

    public void setValueDescription(String valueName, String desc) {
        this.descriptions.put(valueName, desc);
    }

    public void setCategoryDisplayName(String categoryName, String displayName) {
        CategoryConfigScrollAreaEntry e = this.getCategoryEntryByName(categoryName);
        if (e != null) {
            e.displayName = displayName;
        }
    }

    public void setValueDisplayName(String valueName, String displayName) {
        ConfigScrollAreaEntry e = this.getEntryByValueName(valueName);
        if (e != null) {
            e.displayName = displayName;
        }
    }

    protected ConfigScrollAreaEntry getEntryByValueName(String valueName) {
        for (ScrollAreaEntry e : this.configList.getEntries()) {
            if (!(e instanceof ConfigScrollAreaEntry)) continue;
            if (!((ConfigScrollAreaEntry) e).configEntry.getName().equals(valueName)) continue;
            return (ConfigScrollAreaEntry) e;
        }
        return null;
    }

    protected CategoryConfigScrollAreaEntry getCategoryEntryByName(String categoryName) {
        for (ScrollAreaEntry e : this.configList.getEntries()) {
            if (!(e instanceof CategoryConfigScrollAreaEntry)) continue;
            if (!((CategoryConfigScrollAreaEntry) e).category.equals(categoryName)) continue;
            return (CategoryConfigScrollAreaEntry) e;
        }
        return null;
    }

    protected void saveConfig() {
        for (ScrollAreaEntry e : this.configList.getEntries()) {
            if (e instanceof ConfigScrollAreaEntry) {
                ((ConfigScrollAreaEntry) e).onSave();
            }
        }
        this.config.syncConfig();
    }

    protected static void renderDescription(String description, int mouseX, int mouseY) {
        if (description != null) return;
        int width = 10;
        int height = 10;
        String[] desc = description.split("%n%");

        //Getting the longest string from the list to render the background with the correct width
        for (String s : desc) {
            int i = Minecraft.getMinecraft().fontRenderer.getStringWidth(s) + 10;
            if (i > width) {
                width = i;
            }
            height += 10;
        }

        mouseX += 5;
        mouseY += 5;

        if (Minecraft.getMinecraft().currentScreen.width < mouseX + width) {
            mouseX -= width + 10;
        }

        if (Minecraft.getMinecraft().currentScreen.height < mouseY + height) {
            mouseY -= height + 10;
        }

        RenderUtils.setZLevelPre(600);

        renderDescriptionBackground(mouseX, mouseY, width, height);

        GlStateManager.enableBlend();

        int i2 = 5;
        for (String s : desc) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, mouseX + 5, mouseY + i2, Color.WHITE.getRGB());
            i2 += 10;
        }

        RenderUtils.setZLevelPost();

        GlStateManager.disableBlend();

    }

    protected static void renderDescriptionBackground(int x, int y, int width, int height) {
        drawRect(x, y, x + width, y + height, new Color(26, 26, 26, 250).getRGB());
    }

    protected static void colorizeButton(GuiElementButton b) {
        b.setBackgroundColor(new Color(100, 100, 100), new Color(130, 130, 130), new Color(180, 180, 180), new Color(199, 199, 199), 1);
    }

    protected static abstract class ConfigScrollAreaEntry extends ScrollAreaEntry {

        protected ConfigEntry configEntry;
        protected FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        protected String displayName;

        public ConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {
            super(parent);
            this.configEntry = configEntry;
        }

        @Override
        public void renderEntry() {

            int center = this.x + (this.getWidth() / 2);

            drawRect(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight(), ENTRY_BACKGROUND_COLOR.getRGB());

            //Render config entry name
            if (this.displayName != null) {
                int nameWidth = font.getStringWidth(this.displayName);
                drawString(font, this.displayName, center - nameWidth - 10, this.y + 10, Color.WHITE.getRGB());
            } else {
                int nameWidth = font.getStringWidth(this.configEntry.getName());
                drawString(font, this.configEntry.getName(), center - nameWidth - 10, this.y + 10, Color.WHITE.getRGB());
            }


        }

        @Override
        public int getHeight() {
            return 26;
        }

        protected abstract void onSave();

        public static boolean isHeaderFooterHovered() {
            GuiScreen s = Minecraft.getMinecraft().currentScreen;
            if(s == null) return false;
            int mouseX = MouseInput.getMouseX();
            int mouseY = MouseInput.getMouseY();
            int minXHeaderFooter = 0;
            int maxXHeaderFooter = s.width;
            int minYHeader = 0;
            int maxYHeader = 50;
            int minYFooter = s.height - 50;
            int maxYFooter = s.height;
            //HEADER
            if ((mouseX >= minXHeaderFooter) && (mouseX <= maxXHeaderFooter) && (mouseY >= minYHeader) && (mouseY <= maxYHeader)) {
                return true;
            }
            //FOOTER
            return (mouseX >= minXHeaderFooter) && (mouseX <= maxXHeaderFooter) && (mouseY >= minYFooter) && (mouseY <= maxYFooter);
        }

    }

    protected static class StringConfigScrollAreaEntry extends ConfigScrollAreaEntry {

        private GuiElementTextField input;

        public StringConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {

            super(parent, configEntry);

            input = new GuiElementTextField(font, 0, 0, 100, 20, true, null);
            input.setMaxStringLength(10000);
            input.setText(configEntry.getValue());

        }

        @Override
        public void render() {
            super.render();

            int center = this.x + (this.getWidth() / 2);

            input.x = center + 10;
            input.y = this.y + 3;
            input.drawTextBox();
        }

        @Override
        protected void onSave() {

            configEntry.setValue(input.getText());

        }

    }

    protected static class IntegerConfigScrollAreaEntry extends ConfigScrollAreaEntry {

        private GuiElementTextField input;

        public IntegerConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {

            super(parent, configEntry);

            input = new GuiElementTextField(font, 0, 0, 100, 20, true, CharacterFilter.getIntegerCharacterFiler());
            input.setMaxStringLength(10000);
            input.setText(configEntry.getValue());

        }

        @Override
        public void render() {
            super.render();

            int center = this.x + (this.getWidth() / 2);

            input.x = center + 10;
            input.y = this.y + 3;
            input.drawTextBox();

        }

        @Override
        protected void onSave() {

            if (!NumberUtils.isInteger(this.input.getText())) {
                System.out.println("################ ERROR [AtumModCore] ################");
                System.out.println("Unable to save value to config! Invalid value type!");
                System.out.println("Value: " + this.input.getText());
                System.out.println("Variable Type: INTEGER");
                System.out.println("##################################################");
                return;

            }
            configEntry.setValue(input.getText());

        }

    }

    protected static class DoubleConfigScrollAreaEntry extends ConfigScrollAreaEntry {

        private GuiElementTextField input;

        public DoubleConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {

            super(parent, configEntry);

            input = new GuiElementTextField(font, 0, 0, 100, 20, true, CharacterFilter.getDoubleCharacterFiler());
            input.setMaxStringLength(10000);
            input.setText(configEntry.getValue());

        }

        @Override
        public void render() {
            super.render();

            int center = this.x + (this.getWidth() / 2);

            input.x = center + 10;
            input.y = this.y + 3;
            input.drawTextBox();

        }

        @Override
        protected void onSave() {

            if (!NumberUtils.isDouble(this.input.getText())) {
                System.out.println("################ ERROR [AtumModCore] ################");
                System.out.println("Unable to save value to config! Invalid value type!");
                System.out.println("Value: " + this.input.getText());
                System.out.println("Variable Type: DOUBLE");
                System.out.println("##################################################");
                return;

            }
            configEntry.setValue(input.getText());

        }

    }

    protected static class LongConfigScrollAreaEntry extends ConfigScrollAreaEntry {

        private GuiElementTextField input;

        public LongConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {

            super(parent, configEntry);

            input = new GuiElementTextField(font, 0, 0, 100, 20, true, CharacterFilter.getIntegerCharacterFiler());
            input.setMaxStringLength(10000);
            input.setText(configEntry.getValue());

        }

        @Override
        public void render() {
            super.render();

            int center = this.x + (this.getWidth() / 2);

            input.x = center + 10;
            input.y = this.y + 3;
            input.drawTextBox();

        }

        @Override
        protected void onSave() {

            if (!NumberUtils.isLong(this.input.getText())) {
                System.out.println("################ ERROR [AtumModCore] ################");
                System.out.println("Unable to save value to config! Invalid value type!");
                System.out.println("Value: " + this.input.getText());
                System.out.println("Variable Type: LONG");
                System.out.println("##################################################");
               return;

            }
            configEntry.setValue(input.getText());

        }

    }

    protected static class FloatConfigScrollAreaEntry extends ConfigScrollAreaEntry {

        private GuiElementTextField input;

        public FloatConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {

            super(parent, configEntry);

            input = new GuiElementTextField(font, 0, 0, 100, 20, true, CharacterFilter.getDoubleCharacterFiler());
            input.setMaxStringLength(10000);
            input.setText(configEntry.getValue());

        }

        @Override
        public void render() {
            super.render();

            int center = this.x + (this.getWidth() / 2);

            input.x = center + 10;
            input.y = this.y + 3;
            input.drawTextBox();

        }

        @Override
        protected void onSave() {

            if (!NumberUtils.isFloat(this.input.getText())) {
                System.out.println("################ ERROR [AtumModCore] ################");
                System.out.println("Unable to save value to config! Invalid value type!");
                System.out.println("Value: " + this.input.getText());
                System.out.println("Variable Type: FLOAT");
                System.out.println("##################################################");
                return;

            }
            configEntry.setValue(input.getText());

        }

    }

    protected static class BooleanConfigScrollAreaEntry extends ConfigScrollAreaEntry {

        private GuiElementButton toggleBtn;
        private boolean state = false;

        public BooleanConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {

            super(parent, configEntry);

            if (configEntry.getValue().equalsIgnoreCase("true")) {
                state = true;
            }

            toggleBtn = new GuiElementButton(0, 0, 102, 20, "", true, (press) -> {
                if (!isHeaderFooterHovered()) {
                    state = !state;
                    toggleBtn.displayString = state ? "§aEnabled" : "§cDisabled";
                }
            });
            toggleBtn.displayString = state ? "§aEnabled" : "§cDisabled";

            colorizeButton(this.toggleBtn);

        }

        @Override
        public void render() {
            super.render();

            int center = this.x + (this.getWidth() / 2);

            toggleBtn.x = center + 9;
            toggleBtn.y = this.y + 3;
            toggleBtn.drawButton(Minecraft.getMinecraft(), MouseInput.getMouseX(), MouseInput.getMouseY(), Minecraft.getMinecraft().getRenderPartialTicks());

        }

        @Override
        protected void onSave() {

            configEntry.setValue(String.valueOf(this.state));

        }

    }

    protected static class CategoryConfigScrollAreaEntry extends ScrollAreaEntry {

        protected String category;
        protected FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        protected String displayName;

        public CategoryConfigScrollAreaEntry(ScrollArea parent, String category) {
            super(parent);
            this.category = category;
        }

        @Override
        public void renderEntry() {

            int center = this.x + (this.getWidth() / 2);

            drawRect(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight(), ENTRY_BACKGROUND_COLOR.getRGB());

            //Render category title
            if (this.displayName != null) {
                int nameWidth = font.getStringWidth(this.displayName);
                drawString(font, this.displayName, center - (nameWidth / 2), this.y + 10, Color.WHITE.getRGB());
            } else {
                int nameWidth = font.getStringWidth(this.category);
                drawString(font, this.category, center - (nameWidth / 2), this.y + 10, Color.WHITE.getRGB());
            }

        }

        @Override
        public int getHeight() {
            return 30;
        }

    }
}
