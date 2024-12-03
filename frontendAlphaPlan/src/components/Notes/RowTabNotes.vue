<template>
    <div class="flex flex-row w-full h-9 items-center justify-between text-sm border-b">
        <!-- Nom et prénom de l'étudiant -->
        <div class="flex w-1/5 px-6 py-2">
            <p><span>{{ dataEleve["prenom"] }}</span> <span class="uppercase">{{ dataEleve["nom"] }}</span></p>
        </div>
        <!-- Notes de l'étudiant -->
        <div class="flex flex-row w-4/5 justify-between">
            <div v-for="tag in tags" class="flex max-w-[7.69%] min-w-[7.69%] h-full justify-center items-center px-6 py-2"
            :class="setStyleByTag(tag), {'hover:bg-sky-100 cursor-pointer': !restriction && (tag !== 'igSp' && tag !== 'teWo' && tag !== 'inSp' && tag !== 'inPr') && userRole.includes('TEAM_MEMBER'), 'hover:bg-gray-100 cursor-pointer': !restriction && (tag !== 'igSp' && tag !== 'teWo' && tag !== 'inSp' && tag !== 'inPr') && !userRole.includes('TEAM_MEMBER')}"
            @click="openDialog(tag)">
                {{ displayNote(dataEleve[tag]) }}
            </div>
        </div>

        <!-- Dialog pour les notes -->
        <Suspense>
            <DialogNotes :sprintId="sprintId" :type="tagDialog" :visibilite="visibleDialog" :nom="nomDialog" :prenom="prenomDialog"
                @update:visibilite="value => { if (value && value.value !== undefined) visibleDialog.value = value.value; }"
                @update:type="value => { if (value && value.value !== undefined) tagDialog.value = value.value; }"
                @update:nom="value => { if (value && value.value !== undefined) nomDialog.value = value.value; }"
                @update:prenom="value => { if (value && value.value !== undefined) prenomDialog.value = value.value; }"
                @close="closeDialog" />
        </Suspense>
    </div>
</template>

<script setup>
import { ref, defineProps, watch, watchEffect } from 'vue';
import DialogNotes from '../Dialog/DialogNotes.vue';

// Tableau des tag des notes
const tags = ["prMa", "teSo", "spCo", "suPr", "teWo", "teBm", "ssBm", "inSp", "ssPr", "otPr", "inPr", "igSp"];

// Variables
const dataEleve = ref([]);
const sprintId = ref(0);

const tagDialog = ref("");
const nomDialog = ref("");
const prenomDialog = ref("");

const visibleDialog = ref(false);

const userRole = buildRoleString(JSON.parse(localStorage.getItem('roles')));

// Props
const props = defineProps({
    dataEleve: Array,
    sprintId: Number,
    restriction: Boolean
});

// On regarde si l'étudiant a des notes
const restriction = ref(false);
if (props.restriction) {
    restriction.value = props.restriction;
}

// On récupère l'id du sprint
if (props.sprintId) {
    sprintId.value = props.sprintId;
}

// On récupère les notes de l'étudiant
if (props.dataEleve.length) {
    dataEleve.value = props.dataEleve[0];
}

function setStyleByTag(tag) {
    if (tag.includes("Bm") && dataEleve.value[tag] != "-") {
        let temp = dataEleve.value[tag].split(".")[0];
        if (temp.includes("-")) {
            return "text-red-600";
        } else if (temp != "0") {
            return "text-cyan-300";
        }
    }
    switch(tag) {
        case "igSp":
            return "font-CASlalomBold border-l";
        case "teBm":
            return "border-l border-dashed";
        case "ssPr":
            return "border-l";
        case "teWo":
            return "font-CASlalomBold";
        case "inSp":
            return "font-CASlalomBold";
        case "prMa":
            return "border-l";
        default:
            return "";
    };
}

const openDialog = (tag) => {
    if (restriction.value) return;
    if (tag === "igSp") return;
    if (tag === "teWo") return;
    if (tag === "inSp") return;
    if (tag === "inPr") return;
    tagDialog.value = tag;
    nomDialog.value = dataEleve.value["nom"];
    prenomDialog.value = dataEleve.value["prenom"];
    visibleDialog.value = true;
};

const closeDialog = (valeur) => {
    visibleDialog.value = false;
};

// Fonction permettant d'enlever les zéros inutiles après la virgule
const displayNote = (note) => {
    // Si la note n'est pas un nombre, on la retourne telle quelle
    if (note === "-") return "-";

    // On récupère les parties de la note
    let parts = note.split('.');

    // Si la note est un nombre à virgule, on retourne la partie entière et la partie décimale
    if (parts.length > 1) {
        if (parts[1].substring(0, 2) === "00" || parts[1].substring(0, 2) === "0"){
            return parts[0];
        }
        return parts[0] + "." + parts[1].substring(0, 2);
    }
};

// Fonction pour construire une chaîne de caractères des rôles de l'utilisateur
function buildRoleString(roles) {
    let roleString = '';
    roles.forEach(role => {
        roleString += role + '/';
    });
    return roleString;
}

// Watchers
watch(() => props.dataEleve, () => {
    if (props.dataEleve.length) {
        dataEleve.value = props.dataEleve[0];
    }
});
watch(() => props.sprintId, () => {
    if (props.sprintId) {
        sprintId.value = props.sprintId;
    }
});
watch(() => props.restriction, () => {
    if (props.restriction) {
        restriction.value = props.restriction;
    }
});

</script>