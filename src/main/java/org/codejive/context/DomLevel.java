package org.codejive.context;

import java.io.IOException;
import java.util.List;
import org.codejive.context.ciml.dom.ContextDocument;
import org.codejive.context.ciml.dom.PanelElement;
import org.codejive.context.ciml.dom.ScreenElement;
import org.codejive.context.ciml.layout.DomLayouter;
import org.codejive.context.render.Box;
import org.codejive.context.render.BoxRenderer;
import org.codejive.context.styles.Property;
import org.codejive.context.styles.Style;
import org.codejive.context.terminal.Screen;
import org.codejive.context.terminal.Term;

public class DomLevel {
    private final Term term;

    public DomLevel(Term term) {
        this.term = term;
    }

    public static void main(String... args) throws IOException {
        try (Term terminal = Term.create()) {
            new LowLevel(terminal).run();
        }
    }

    public int run() throws IOException {
        Screen screen = term.fullScreen();
        int displayWidth = screen.rect().width();
        int displayHeight = screen.rect().height();

        ContextDocument doc = new ContextDocument();
        ScreenElement scr =
                doc.append(doc.createScreenElement(), size(displayWidth, displayHeight));
        PanelElement pan = scr.append(doc.createPanelElement(), pos(5, 5).and(size(20, 10)));

        List<Box> boxes = new DomLayouter().layout(doc);
        BoxRenderer boxr = new BoxRenderer(screen);
        for (Box b : boxes) {
            boxr.render(b);
        }
        screen.update();
        int c = term.input().readChar();

        return 0;
    }

    private Style pos(int x, int y) {
        Style s = new Style();
        s.putAsEmInt(Property.left, x);
        s.putAsEmInt(Property.top, y);
        return s;
    }

    private Style size(int w, int h) {
        Style s = new Style();
        s.putAsEmInt(Property.width, w);
        s.putAsEmInt(Property.height, h);
        return s;
    }
}
