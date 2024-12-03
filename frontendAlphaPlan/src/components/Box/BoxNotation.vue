<template>
  <div class="font-Roboto flex flex-col border border-custom-grey px-2 py-2 mx-3 rounded-md">
    <h2 class="text-sm font-CASlalomBold">{{ title }}</h2>
    <p class="text-xs font-Roboto text-custom-grey">{{ text }}</p>
    <div class="flex justify-center">
      <InputNumber :percentageMode="false" @update:modelValue="emitNote" v-model="note" />
    </div>
    <textarea @input="emitCommentaire" v-model="commentaire" placeholder="Écrire un commentaire..."
      class="w-full h-20 mt-2 border border-gray-300 bg-gray-100 rounded-md p-2 text-sm italic-placeholder::placeholder">
        </textarea>
  </div>
</template>

<script setup>
import { ref, defineEmits, watch } from 'vue';
import InputNumber from '../InputNumber.vue';

const props = defineProps({
  title: String,
  text: String,
  note: Number,
  commentaire: String,
});

const title = ref(props.title || 'Titre par défaut');
const text = ref(props.text);
const note = ref(props.note);
const commentaire = ref(props.commentaire);


const emits = defineEmits(['noteChanged', 'commentaireChanged']);


watch(() => props.note, (newNote) => {
  note.value = newNote;
});

watch(() => props.commentaire, (newCommentaire) => {
  commentaire.value = newCommentaire;
});

// Fonction pour émettre l'événement lorsque la note est modifiée
function emitNote(newNote) {
  note.value = newNote;
  emits('noteChanged', newNote);
}

// Fonction pour émettre l'événement lorsque le commentaire est modifié
function emitCommentaire(event) {
  const newCommentaire = event.target.value;
  commentaire.value = newCommentaire;
  emits('commentaireChanged', newCommentaire);
}



</script>