const API = '/tarefas';

const lista = document.getElementById('lista');
const totalEl = document.getElementById('total');
const form = document.getElementById('form');

let tarefasCache = [];

form.addEventListener('submit', salvar);
carregar();

/* =======================
   LISTAR
======================= */
function carregar() {
    fetch(API)
        .then(res => {
            if (!res.ok) {
                throw new Error('Erro ao buscar tarefas');
            }
            return res.json();
        })
        .then(dados => {
            if (!Array.isArray(dados)) {
                throw new Error('Resposta não é lista');
            }

            tarefasCache = dados;
            lista.innerHTML = '';
            let total = 0;

            dados.forEach(tarefa => {
                total += Number(tarefa.custo);

                const tr = document.createElement('tr');
                if (tarefa.custo >= 1000) tr.classList.add('alto-custo');

                tr.innerHTML = `
                    <td>${tarefa.nome}</td>
                    <td>R$ ${Number(tarefa.custo).toFixed(2)}</td>
                    <td>${formatarData(tarefa.dataLimite)}</td>
                    <td>
                        <button onclick="editar(${tarefa.id})">Editar</button>
                        <button onclick="excluirTarefa(${tarefa.id})">Excluir</button>
                        <button onclick="subir(${tarefa.id})">⬆</button>
                        <button onclick="descer(${tarefa.id})">⬇</button>
                    </td>
                `;

                lista.appendChild(tr);
            });

            totalEl.innerText = `Total: R$ ${total.toFixed(2)}`;
        })
        .catch(err => console.error(err.message));
}

/* =======================
   SALVAR (POST / PUT)
======================= */
function salvar(e) {
    e.preventDefault();

    const id = document.getElementById('id').value;

    const tarefa = {
        nome: document.getElementById('nome').value,
        custo: Number(document.getElementById('custo').value),
        dataLimite: document.getElementById('dataLimite').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API}/${id}` : API;

    fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(tarefa)
    })
        .then(res => {
            if (!res.ok) throw new Error('Erro ao salvar');
            return res.json();
        })
        .then(() => {
            form.reset();
            document.getElementById('id').value = '';
            carregar();
        })
        .catch(err => alert(err.message));
}

/* =======================
   EDITAR
======================= */
function editar(id) {
    const tarefa = tarefasCache.find(t => t.id === id);
    if (!tarefa) return;

    document.getElementById('id').value = tarefa.id;
    document.getElementById('nome').value = tarefa.nome;
    document.getElementById('custo').value = tarefa.custo;
    document.getElementById('dataLimite').value = tarefa.dataLimite;
}

/* =======================
   EXCLUIR
======================= */
function excluirTarefa(id) {
    if (!confirm('Deseja excluir a tarefa?')) return;

    fetch(`${API}/${id}`, { method: 'DELETE' })
        .then(res => {
            if (!res.ok) throw new Error('Erro ao excluir');
            carregar();
        })
        .catch(err => alert(err.message));
}

/* =======================
   ORDENAR
======================= */
function subir(id) {
    fetch(`${API}/${id}/subir`, { method: 'PUT' })
        .then(() => carregar());
}

function descer(id) {
    fetch(`${API}/${id}/descer`, { method: 'PUT' })
        .then(() => carregar());
}

/* =======================
   UTIL
======================= */
function formatarData(data) {
    return new Date(data).toLocaleDateString('pt-BR');
}