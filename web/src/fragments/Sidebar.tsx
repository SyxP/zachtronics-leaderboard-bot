/*
 * Copyright (c) 2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Box, Divider, List, ListItemText, useTheme, Link as ExternalLink } from "@mui/material"
import ExpandableListItem from "../components/ExpandableListItem"
import { Extension, Folder } from "@mui/icons-material"
import CategoryIcon from "@mui/icons-material/Category"
import { SxProps } from "@mui/system"
import { Theme } from "@mui/material/styles"
import LinkListItem from "../components/LinkListItem"
import Group from "../model/Group"
import Puzzle from "../model/Puzzle"
import Category from "../model/Category"
import { Link, useMatch } from "react-router-dom"
import ApiResource from "../utils/ApiResource"

function Campaign() {
    const match = useMatch("/puzzles/[P007|P010|P009|P011|P013|P008|P012|P014|P015|P016|P019|P018|P017|P020|P021|P022|P024|P025|P026|P027|P028|P030b|P029|P031b|P034|P033|P032|P036|P035|P037|P038|P042|P039|P040|P041|P043|P076|P080|P075|P074|P083|P078|P079|P081|P082|P077|P084]/*")
    const CampaignChapters = ["CHAPTER_1", "CHAPTER_2", "CHAPTER_3", "CHAPTER_4",
                              "CHAPTER_5", "CHAPTER_PRODUCTION"]
    return (
        <List>
            <ExpandableListItem
                title={"Campaign Puzzles"}
                icon={<Extension />}
                content={ApiResource<Puzzle[]>({
                    url: "/om/puzzles",
                    element: (puzzles) => (
                        <>
                            {[
                                ...puzzles
                                    .reduce<Map<string, Puzzle[]>>((acc, puzzle) => {
                                        if (!acc.has(puzzle.group.id)) acc.set(puzzle.group.id, [puzzle])
                                        else acc.get(puzzle.group.id)!.push(puzzle)
                                        return acc
                                    }, new Map())
                                    .entries(),
                            ].filter(([group, _]) => (CampaignChapters.includes(group)))
                            .map(([group, puzzles]) => (
                                <Puzzles group={puzzles[0].group} puzzles={puzzles} key={group} sx={{ pl: 4 }} />
                            ))}
                        </>
                    ),
                })}
                open={match !== null}
            />
        </List>
    )
}

function Journal() {
    const match = useMatch("/puzzles/[P054|P055|P056|P057|P058|P059|P060|P061|P062|P063|P064|P065|P066|P067|P068|P069|P070|P071|P072|P086|P088|P085|P087|P089|P091b|P090|P092|P093|P094|P096|P095|P097|P098|P099|P100|P101|P102|P104|P103|P106|P105|P109|P108|P107]/*")
    const JournalChapters = ["JOURNAL_I", "JOURNAL_II", "JOURNAL_III", "JOURNAL_IV",
                             "JOURNAL_V", "JOURNAL_VI", "JOURNAL_VII", "JOURNAL_VIII",
                             "JOURNAL_IX"]
    return (
        <List>
            <ExpandableListItem
                title={"Journal Puzzles"}
                icon={<Extension />}
                content={ApiResource<Puzzle[]>({
                    url: "/om/puzzles",
                    element: (puzzles) => (
                        <>
                            {[
                                ...puzzles
                                    .reduce<Map<string, Puzzle[]>>((acc, puzzle) => {
                                        if (!acc.has(puzzle.group.id)) acc.set(puzzle.group.id, [puzzle])
                                        else acc.get(puzzle.group.id)!.push(puzzle)
                                        return acc
                                    }, new Map())
                                    .entries(),
                            ].filter(([group, _]) => (JournalChapters.includes(group)))
                            .map(([group, puzzles]) => (
                                <Puzzles group={puzzles[0].group} puzzles={puzzles} key={group} sx={{ pl: 4 }} />
                            ))}
                        </>
                    ),
                })}
                open={match !== null}
            />
        </List>
    )
}

function Community() {
    const match = useMatch("/puzzles/w*")
    const CampaignChapters = ["CHAPTER_1", "CHAPTER_2", "CHAPTER_3", "CHAPTER_4",
                              "CHAPTER_5", "CHAPTER_PRODUCTION"]
    const JournalChapters = ["JOURNAL_I", "JOURNAL_II", "JOURNAL_III", "JOURNAL_IV",
                             "JOURNAL_V", "JOURNAL_VI", "JOURNAL_VII", "JOURNAL_VIII",
                             "JOURNAL_IX"]
    return (
        <List>
            <ExpandableListItem
                title={"Community Puzzles"}
                icon={<Extension />}
                content={ApiResource<Puzzle[]>({
                    url: "/om/puzzles",
                    element: (puzzles) => (
                        <>
                            {[
                                ...puzzles
                                    .reduce<Map<string, Puzzle[]>>((acc, puzzle) => {
                                        if (!acc.has(puzzle.group.id)) acc.set(puzzle.group.id, [puzzle])
                                        else acc.get(puzzle.group.id)!.push(puzzle)
                                        return acc
                                    }, new Map())
                                    .entries(),
                            ].filter(([group, _]) => (!CampaignChapters.includes(group) && !JournalChapters.includes(group)))
                            .map(([group, puzzles]) => (
                                <Puzzles group={puzzles[0].group} puzzles={puzzles} key={group} sx={{ pl: 4 }} />
                            ))}
                        </>
                    ),
                })}
                open={match !== null}
            />
        </List>
    )
}

interface PuzzlesProps {
    group: Group
    puzzles: Puzzle[]
    sx?: SxProps<Theme>
}

function Puzzles(props: PuzzlesProps) {
    const match = useMatch("/puzzles/:puzzleId/*")
    const puzzleId = match?.params?.puzzleId
    return (
        <List sx={props.sx}>
            <ExpandableListItem
                title={props.group.displayName}
                icon={<Folder />}
                content={
                    <>
                        {props.puzzles.map((puzzle) => (
                            <LinkListItem sx={{ pl: 4 }} key={puzzle.id} to={`/puzzles/${puzzle.id}`} selected={puzzleId === puzzle.id}>
                                <ListItemText primary={puzzle.displayName} />
                            </LinkListItem>
                        ))}
                    </>
                }
                open={props.puzzles.some((puzzle) => puzzleId === puzzle.id)}
            />
        </List>
    )
}

function Categories() {
    const match = useMatch("/categories/:categoryId/*")
    const categoryId = match?.params?.categoryId
    return (
        <List>
            <ExpandableListItem
                title={"Categories"}
                icon={<CategoryIcon />}
                content={ApiResource<Category[]>({
                    url: "/om/categories",
                    element: (categories) => (
                        <>
                            {categories
                                .sort((a, b) => (a.puzzleTypes.includes("PRODUCTION") && !b.puzzleTypes.includes("PRODUCTION") ? 1 : -1))
                                .map((category) => (
                                    <LinkListItem sx={{ pl: 4 }} key={category.id} to={`/categories/${category.id}`} selected={categoryId === category.id}>
                                        <ListItemText primary={`${category.displayName} (${category.metrics.join("→")})${category.puzzleTypes.includes("PRODUCTION") ? " (Production)" : ""}`} />
                                    </LinkListItem>
                                ))}
                        </>
                    ),
                })}
                open={categoryId !== undefined}
            />
        </List>
    )
}

export default function Sidebar() {
    const theme = useTheme()
    // noinspection HtmlUnknownTarget
    return (
        <>
            <Campaign />
            <Journal />
            <Community />
            <Divider />
            <Categories />
            <Box sx={{ flexGrow: 1 }} />
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                }}
            >
                <Link to="/help" style={{ padding: "1rem", color: theme.palette.primary.main }}>
                    Help
                </Link>
                <ExternalLink href="/swagger-ui.html" style={{ padding: "1rem", color: theme.palette.primary.main }}>
                    API docs
                </ExternalLink>
            </Box>
        </>
    )
}
