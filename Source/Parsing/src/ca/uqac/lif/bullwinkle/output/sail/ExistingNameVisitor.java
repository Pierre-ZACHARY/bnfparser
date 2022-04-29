package ca.uqac.lif.bullwinkle.output.sail;

import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.output.OutputFormatVisitor;

/**
 * D'après un Visiteur "Variable Definition Visitor", vérifie que toutes les variables utilisées dans le fichier ( selon le token "existing-name" ), sont bien définit précédemment
 * S'il n'y a pas de variable du même nom déjà définies, on renverra une liste de celles qui peuvent match ( celle du même type )
 */
public class ExistingNameVisitor implements OutputFormatVisitor {

    @Override
    public void visit(ParseNode node) throws VisitException {

    }

    @Override
    public void pop() {

    }

    @Override
    public String toOutputString() {
        return null;
    }
}
