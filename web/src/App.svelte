<script>
    const fetch_retry = async (url, options, n) => {
        try {
            return await fetch(url, options)
        } catch (err) {
            if (n === 1) throw err;
            return await fetch_retry(url, options, n - 1);
        }
    };

    const player = "gyiw9173"
    const apiKey = "RGAPI-f7d7f384-e3f3-44f8-8932-d4a732623daf";
    const maxGame = 2000;

    let playersFromBase;

    const getMyTeamId = (participants) => {
        return participants.find(p => player === p.player.summonerName).participantId;
    }

    const isAlly = (myTeamId, participantId) => {
        if (myTeamId <= 5) {
            return participantId <= 5;
        }
        return participantId > 5;
    }

    const isWon = (myTeamId, match) => {
        if (myTeamId <= 5) {
            return match.teams[0].win === "Win";
        }
        return match.teams[1].win === "Win";
    }

    const fetchSummonerId = (async () => {
        const response = await fetch(`/api/lol/summoner/v4/summoners/by-name/${player}?api_key=${apiKey}`)
        return await response.json();
    })

    const fetchMatches = (async () => {
        let summonerIdRequest = await fetchSummonerId();
        let accountId = summonerIdRequest.accountId;
        const response = await fetch(`/api/lol/match/v4/matchlists/by-account/${accountId}?api_key=${apiKey}`)
        return await response.json()
    })

    const fetchPlayers = (async () => {
        let requests = await fetchMatches();
        let requestMatches = requests.matches.slice(0, maxGame);
        for (const request of requestMatches) {
            if (playersFromBase.find(p => p.games.find(g => g.gameId === request.gameId)) != null) {
                console.log(request.gameId + " already in db");
                continue;
            }
            console.log(request);
            const response = await fetch_retry(`/api/lol/match/v4/matches/${request.gameId}?api_key=${apiKey}`)
            const match = await response.json();

            let myTeamId = getMyTeamId(match.participantIdentities);
            for (let participant of match.participantIdentities) {
                let summonerName = participant.player.summonerName;
                if (summonerName === player) {
                    continue;
                }
                const matchCopy = JSON.parse(JSON.stringify(match))
                matchCopy.isAlly = isAlly(myTeamId, participant.participantId);
                matchCopy.isWon = isWon(myTeamId, match);

                await fetch(`/store/player`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({name: summonerName, games: [matchCopy]})
                });
            }
        }
    });

    const loadInfo = () => {
        fetchPlayers();
    };

    const viewInfo = (async () => {
        let result = await fetch(`/store/players`);

        return playersFromBase = await result.json();
    })();

</script>

<button on:click={loadInfo}>
    Load info
</button>

{#await viewInfo}
    <p>...waiting </p>
{:then players}
    <ul>
        {#each players as player}
            <li>
                <a href="https://euw.op.gg/summoner/userName={player.name}" target="_blank">{player.name}</a> :
                <ul>
                    {#each player.games as game}
                        <li>
                            <a href="https://matchhistory.euw.leagueoflegends.com/en/#match-details/EUW1/{game.gameId}/228936900?tab=overview"
                               target="_blank">{game.gameId}</a>
                            ({#if game.isAlly} Ally{:else} Enemy{/if})
                            ({#if game.isWon} Win{:else} Loose{/if})
                        </li>
                    {/each}
                </ul>
            </li>
        {/each}
    </ul>
{:catch error}
    <p>An error occurred!</p>
{/await}