import { customFetch } from "../utils/FetchUtil";

export async function fetchNotesSprint(eleveId, sprintId) {
    const response = await customFetch(`sprints/notes/${eleveId}/${sprintId}`);

    const data = await response.json();
    return data.map((note) => {
        return {
            ...note,
            tag: note.tag,
            nom: note.nom,
            note: note.note,
            coeff: note.coeff
        };
    });
}

export async function fetchNoteByTypeAndNames(nom, prenom, sprintId, type) {
    let pathName = 'notesEquipe';
    type = type.toUpperCase();
    if (type === 'SSPR' || type === 'INPR' || type === 'IGSP' || type === 'INSP' || type === 'SSBM' || type === 'TEBM') {
        pathName = 'notesEleve';
    }
    const response = await customFetch('v1.0/' + pathName + '/' + type + '/' + sprintId + '/' + nom + '/' + prenom);
    return await response.json();
}

export async function fetchNoteByTypeAndId(eleveId, sprintId, type) {
    let pathName = 'notesEquipe';
    type = type.toUpperCase();
    type = type.replace('_', '');
    if (type === 'SSPR' || type === 'INPR' || type === 'IGSP' || type === 'INSP' || type === 'SSBM' || type === 'TEBM') {
        pathName = 'notesEleve';
    }
    const response = await customFetch('v1.0/' + pathName + '/' + type + '/' + sprintId + '/' + eleveId);
    return await response.json();
}

export async function fetchNotesEquipe(equipeId, sprintId) {
    const response = await customFetch(`sprints/notes_equipe/${equipeId}/${sprintId}`);

    const data = await response.json();
    return data.map((note) => {
        return {
            nom: note.nom,
            prenom: note.prenom,
            prMa: note.prMa,
            teSo: note.teSo,
            spCo: note.spCo,
            suPr: note.suPr,
            teWo: note.teWo,
            teBm: note.teBm,
            ssBm: note.ssBm,
            inSp: note.inSp,
            ssPr: note.ssPr,
            otPr: note.otPr,
            inPr: note.inPr,
            igSp: note.igSp
        };
    });
}

export async function getEquipesMoyennes() {
    return await customFetch('v1.0/notesEquipe/moyennes');
}