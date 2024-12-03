<template>
  <div>
    <button @click="exportToCSV" class="download-button">Télécharger la liste des étudiants</button>
  </div>
</template>

<style scoped>
.download-button {
  background-color: #4CAF50;
  border: none;
  color: white;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  transition-duration: 0.4s;
  cursor: pointer;
  border-radius: 8px;
}

.download-button:hover {
  background-color: #45a049;
}

/* Style lorsque le bouton est enfoncé */
.download-button:active {
  background-color: #3e8e41;
}
</style>

  <script>
  import {useToast} from 'vue-toastification';
  import {customFetch} from "../utils/FetchUtil";

  const toast = useToast();

  export default {
    methods: {
      async exportToCSV() {
        try {
          const response = await customFetch('users/exportCSV', {
            method: 'GET',
            headers: {
              'Content-Type': 'text/csv',
            },
          });
          const blob = await response.blob();
          const url = window.URL.createObjectURL(new Blob([blob]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'Etudiant.csv');
          document.body.appendChild(link);
          link.click();
          window.URL.revokeObjectURL(url);
        } catch (error) {
          toast.error("Une erreur s'est produite lors du téléchargement du fichier !", {position: 'top-center'});
        }
      },
    },
  };
  </script>
  